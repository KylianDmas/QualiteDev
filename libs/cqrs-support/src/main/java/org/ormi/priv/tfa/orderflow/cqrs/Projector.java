package org.ormi.priv.tfa.orderflow.cqrs;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.StreamSupport;

/**
 * Projector interface for projecting events onto a state.
 * 
 * @author Thibaud FAURIE
 */
public interface Projector<S, E extends EventEnvelope<? extends DomainEvent>> {
    /**
     * Projects an event onto the current state.
     * 
     * @param current the current state
     * @param ev      the event to project
     * @return the projected state
     */
    ProjectionResult<S> project(Optional<S> current, E ev);

    /**
     * Projects a list of events onto the current state.
     * 
     * @param current the current state
     * @param events  the events to project
     * @return the projected state
     */
    default ProjectionResult<S> projectAll(Optional<S> current, Iterable<E> events) {
        return projectAll(current, events, Optional.empty());
    }

    /**
     * Projects a list of events onto the current state with the given initial version.
     * 
     * @param current the current state
     * @param events  the events to project
     * @param initialVersion the initial version
     * @return the projected state
     */
    default ProjectionResult<S> projectAll(Optional<S> current, Iterable<E> events, Long initialVersion) {
        return projectAll(current, events, Optional.of(initialVersion));
    }

    /**
     * Projects a list of events onto the current state.
     * 
     * @param current the current state
     * @param events  the events to project
     * @param initialVersion the initial version
     * @return the projected state
     */
    default ProjectionResult<S> projectAll(Optional<S> current, Iterable<E> events,
            Optional<Long> initialVersion) {
        Acc<S> init = new Acc<>(ProjectionResult.projected(current.orElse(null)),
                initialVersion.orElse(0L));

        return StreamSupport.stream(events.spliterator(), false)
                .reduce(
                        init,
                        (acc, ev) -> {
                            if (acc.result().isFailure())
                                return acc;
                            if (ev.sequence() <= acc.lastSequence())
                                return acc;

                            ProjectionResult<S> next = acc.result().flatMap(state -> project(Optional.ofNullable(state), ev));

                            return new Acc<>(next, ev.sequence());
                        },
                        (a1, a2) -> {
                            throw new UnsupportedOperationException(
                                    "Parallel stream not supported for sequential projections");
                        })
                .result();
    }

    /**
     * Accumulator for projection results.
     */
    record Acc<S>(ProjectionResult<S> result, long lastSequence) {
    }

    /**
     * Projection result. A monadic type that carries the result of a projection
     * either
     * successfully or with an error.
     *
     * @param <S> the type of the projected state
     */
    public static class ProjectionResult<S> {
        /**
         * The projected state.
         */
        private final S projectedState;
        /**
         * The error message, if any.
         */
        private final String err;
        /**
         * The reason why the projection was a no-op, if any.
         */
        private final String noopReason;

        /**
         * Private constructor for creating a projection result.
         *
         * @param projectedState the projected state
         * @param err            the error message, if any
         */
        private ProjectionResult(S projectedState, String err, String noopReason) {
            this.projectedState = projectedState;
            this.err = err;
            this.noopReason = noopReason;
        }

        /**
         * Creates a successful projection result.
         *
         * @param <S>            the type of the projected state
         * @param projectedState the projected state
         * @return a successful projection result
         */
        public static <S> ProjectionResult<S> projected(S projectedState) {
            return new ProjectionResult<>(projectedState, null, null);
        }

        /**
         * Creates a failed projection result.
         *
         * @param <S> the type of the projected state
         * @param err the error message
         * @return a failed projection result
         */
        public static <S> ProjectionResult<S> failed(String err) {
            return new ProjectionResult<>(null, err, null);
        }

        /**
         * Creates a no-op projection result.
         *
         * @param <S> the type of the projected state
         * @param noopReason the reason why the projection was a no-op
         * @return a no-op projection result
         */
        public static <S> ProjectionResult<S> noOp(String noopReason) {
            return new ProjectionResult<>(null, null, noopReason);
        }

        /**
         * Gets the projected state.
         *
         * @return the projected state
         */
        public S getProjection() {
            return projectedState;
        }

        /**
         * Gets the error message, if failed.
         *
         * @return the error message, if failed.
         */
        public String getError() {
            return err;
        }

        /**
         * Gets the reason why the projection was a no-op, if any.
         *
         * @return the reason why the projection was a no-op, if any
         */
        public String getNoopReason() {
            return noopReason;
        }

        /**
         * Checks if the projection was successful.
         *
         * @return true if the projection was successful, false otherwise
         */
        public boolean isSuccess() {
            return err == null && projectedState != null;
        }

        /**
         * Checks if the projection failed.
         *
         * @return true if the projection failed, false otherwise
         */
        public boolean isFailure() {
            return err != null && projectedState == null;
        }

        /**
         * Checks if the projection was a no-op.
         *
         * Means the projection did not change the state.
         *
         * @return true if the projection was a no-op, false otherwise
         */
        public boolean isNoOp() {
            return noopReason != null && projectedState == null && err == null;
        }

        /**
         * Maps the projected state to a new value.
         *
         * @param f the mapping function
         * @return a new projection result with the mapped value, or the original error
         *         if failed
         */
        public ProjectionResult<S> map(Function<S, S> f) {
            return isSuccess() ? projected(f.apply(projectedState))
                    : failed(err);
        }

        /**
         * Maps the projected state to a new value.
         *
         * @param <T> the type of the new projected state
         * @param f   the mapping function
         * @return a new projection result with the mapped value, or the original error
         *         if failed
         */
        public <T> ProjectionResult<T> flatMap(Function<S, ProjectionResult<T>> f) {
            return isSuccess() ? f.apply(projectedState)
                    : failed(err);
        }
    }
}

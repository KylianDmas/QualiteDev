package org.ormi.priv.tfa.orderflow.productregistry.read.infra.outbox;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.jboss.logging.Logger;
import org.ormi.priv.tfa.orderflow.cqrs.Projector.ProjectionResult;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.OutboxEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.OutboxRepository;
import org.ormi.priv.tfa.orderflow.kernel.common.AggregateType;
import org.ormi.priv.tfa.orderflow.kernel.product.jpa.ProductEventJpaMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductEventVersion;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView;
import org.ormi.priv.tfa.orderflow.productregistry.read.application.ProjectionDispatcher;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * TODO: Complete Javadoc
 */

@ApplicationScoped
@Startup
public class OutboxPartitionedPoller {

    private static final int PARTITIONS = Runtime.getRuntime().availableProcessors();
    private static final int BATCH_SIZE = 10;
    private static final int POLL_INTERVAL_MS = 1000;
    private static final int MAX_RETRIES = 3;
    private static final Duration RETRY_DELAY = Duration.ofSeconds(30);

    private static final Logger LOG = Logger.getLogger(OutboxPartitionedPoller.class);

    private final ScheduledExecutorService pollScheduler = Executors.newSingleThreadScheduledExecutor();
    private ExecutorService[] executors = IntStream.range(0, PARTITIONS)
            .mapToObj(i -> Executors.newSingleThreadExecutor(r -> new Thread(r, "outbox-poller-" + i)))
            .toArray(ExecutorService[]::new);

    private final Map<UUID, Instant> blockedUntil = new ConcurrentHashMap<>();

    private final OutboxRepository outbox;
    private final ProjectionDispatcher dispatcher;
    private final ProductEventJpaMapper mapper;

    @Inject
    public OutboxPartitionedPoller(
            OutboxRepository outboxRepository,
            ProjectionDispatcher dispatcher,
            ProductEventJpaMapper mapper) {
        this.outbox = outboxRepository;
        this.dispatcher = dispatcher;
        this.mapper = mapper;
    }

    void onStart(@Observes StartupEvent event) {
        pollScheduler.scheduleWithFixedDelay(this::poll, 0, POLL_INTERVAL_MS, TimeUnit.MILLISECONDS);
        // TODO: Hey, log some info
        LOG.info("OutboxPartitionedPoller started with " + PARTITIONS + " partitions.");
    }

    void onStop(@Observes ShutdownEvent event) {
        pollScheduler.shutdownNow();
        Arrays.stream(executors).forEach(ExecutorService::shutdownNow);
        // TODO: Hey, log some info
    }

    @ActivateRequestContext
    @Transactional
    protected void poll() {
        try {
            List<OutboxEntity> readyMessages = outbox
                    .fetchReadyByAggregateTypeOrderByAggregateVersion(AggregateType.PRODUCT.value(), BATCH_SIZE,
                            MAX_RETRIES);
            if (readyMessages.isEmpty())
                return;
            readyMessages.forEach((msg) -> {
                UUID aggregateId = msg.getSourceEvent().getAggregateId();
                Instant blockedTime = blockedUntil.get(aggregateId);
                if (blockedTime != null && blockedTime.isAfter(Instant.now())) {
                    // Still blocked, skip processing
                    return;
                }
                // Get corresponding partition
                int partition = Math.floorMod(aggregateId.hashCode(), PARTITIONS);
                executors[partition].submit(() -> process(msg));
            });
        } catch (Exception e) {
            LOG.error("Error occurred while polling outbox messages", e);
        }
    }

    private void process(OutboxEntity outboxMsg) {
        var ev = outboxMsg.getSourceEvent();
        try {
            if (ev.getEventVersion() == ProductEventVersion.V1.getValue()) {
                final ProjectionResult<ProductView> result = dispatcher.dispatch(
                        mapper.toProductEventV1(ev));
                if (result.isSuccess()) {
                    outbox.delete(outboxMsg);
                    return;
                }
                if (result.isNoOp()) {
                    outbox.markFailed(outboxMsg, result.getNoopReason(),
                            Long.valueOf(RETRY_DELAY.toMillis()).intValue());
                }
                if (result.isFailure()) {
                    outbox.markFailed(outboxMsg, result.getError(), Long.valueOf(RETRY_DELAY.toMillis()).intValue());
                }
                blockedUntil.put(outboxMsg.getSourceEvent().getAggregateId(), Instant.now().plus(RETRY_DELAY));
            }
        } catch (Exception e) {
            LOG.error(String.format("ProjectionDispatcher failed for outbox message id=%d, aggregateId=%s: %s",
                    outboxMsg.getId(), ev.getAggregateId(), e.getMessage()), e);
            outbox.markFailed(outboxMsg, e.getMessage(), Long.valueOf(RETRY_DELAY.toMillis()).intValue());
            blockedUntil.put(outboxMsg.getSourceEvent().getAggregateId(), Instant.now().plus(RETRY_DELAY));
        }
    }
}

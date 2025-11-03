package org.ormi.priv.tfa.orderflow.cqrs;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

public interface DomainEvent {
    default String eventType() {
        return this.getClass().getSimpleName();
    }
    UUID aggregateId();
    String aggregateType();
    int version();
    DomainEventPayload payload();

    @JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
    )
    public static interface DomainEventPayload {
        public static record Empty() implements DomainEventPayload {
        }
    }
}

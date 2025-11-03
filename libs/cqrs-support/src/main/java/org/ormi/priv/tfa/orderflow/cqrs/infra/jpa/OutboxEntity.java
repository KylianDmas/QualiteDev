package org.ormi.priv.tfa.orderflow.cqrs.infra.jpa;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * TODO: Complete Javadoc
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(schema = "eventing", name = "outbox", indexes = {
        @Index(name = "ix_outbox_ready", columnList = "next_attempt_at")
})
public class OutboxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "bigserial")
    private Long id;
    @Column(name = "attempts", nullable = false, updatable = false, columnDefinition = "int")
    private int attempts;
    @Column(name = "next_attempt_at", nullable = false, updatable = false, columnDefinition = "timestamptz")
    private Instant nextAttemptAt;
    @Column(name = "last_error", nullable = false, updatable = false, columnDefinition = "text")
    private String lastError;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false, updatable = false, columnDefinition = "bigint")
    private EventLogEntity sourceEvent;

    public static OutboxEntityBuilder Builder() {
        return new OutboxEntityBuilder();
    }

    public static class OutboxEntityBuilder {
        private EventLogEntity sourceEvent;

        public OutboxEntityBuilder sourceEvent(EventLogEntity evt) {
            this.sourceEvent = evt;
            return this;
        }

        public OutboxEntity build() {
            OutboxEntity entity = new OutboxEntity();
            entity.sourceEvent = sourceEvent;
            return entity;
        }
    }
}

package org.ormi.priv.tfa.orderflow.cqrs.infra.persistence;

import java.util.List;

import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.OutboxEntity;

/**
 * TODO: Complete Javadoc
 */

public interface OutboxRepository {
    void publish(OutboxEntity entity);
    List<OutboxEntity> fetchReadyByAggregateTypeOrderByAggregateVersion(String aggregateType, int limit, int maxRetries);
    void delete(OutboxEntity entity);
    void markFailed(OutboxEntity entity, String err);
    void markFailed(OutboxEntity entity, String err, int retryAfter);
}

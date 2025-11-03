package org.ormi.priv.tfa.orderflow.kernel.product.jpa;

import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.EventLogEntity;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductDescriptionUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductNameUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductRegistered;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductRetired;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1Envelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1Envelope.ProductDescriptionUpdatedEnvelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1Envelope.ProductNameUpdatedEnvelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1Envelope.ProductRegisteredEnvelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1Envelope.ProductRetiredEnvelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductEventVersion;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductEventJpaMapper {

    static final String SKUID_FIELD = "skuId";
    static final String NAME_FIELD = "name";
    static final String DESCRIPTION_FIELD = "description";
    static final String OLD_NAME_FIELD = "oldName";
    static final String NEW_NAME_FIELD = "newName";
    static final String OLD_DESCRIPTION_FIELD = "oldDescription";
    static final String NEW_DESCRIPTION_FIELD = "newDescription";

    public ProductEventV1Envelope<?> toProductEventV1(EventLogEntity entity) {
        if (entity.getEventVersion() != ProductEventVersion.V1.getValue()) {
            throw new IllegalArgumentException("Unsupported event version: " + entity.getEventVersion());
        }
        if (entity.getEventType().equals(ProductRegistered.class.getSimpleName())) {
            return new ProductRegisteredEnvelope(
                new ProductRegistered(
                    new ProductId(entity.getAggregateId()),
                    new SkuId(entity.getPayload().get(SKUID_FIELD).asText()),
                    entity.getPayload().get(NAME_FIELD).asText(),
                    entity.getPayload().get(DESCRIPTION_FIELD).asText()
                ),
                entity.getAggregateVersion(),
                entity.getOccurredAt()
            );
        }
        if (entity.getEventType().equals(ProductRetired.class.getSimpleName())) {
            return new ProductRetiredEnvelope(
                new ProductRetired(
                    new ProductId(entity.getAggregateId())
                ),
                entity.getAggregateVersion(),
                entity.getOccurredAt()
            );
        }
        if (entity.getEventType().equals(ProductNameUpdated.class.getSimpleName())) {
            return new ProductNameUpdatedEnvelope(
                new ProductNameUpdated(
                    new ProductId(entity.getAggregateId()),
                    entity.getPayload().get(OLD_NAME_FIELD).asText(),
                    entity.getPayload().get(NEW_NAME_FIELD).asText()
                ),
                entity.getAggregateVersion(),
                entity.getOccurredAt()
            );
        }
        if (entity.getEventType().equals(ProductDescriptionUpdated.class.getSimpleName())) {
            return new ProductDescriptionUpdatedEnvelope(
                new ProductDescriptionUpdated(
                    new ProductId(entity.getAggregateId()),
                    entity.getPayload().get(OLD_DESCRIPTION_FIELD).asText(),
                    entity.getPayload().get(NEW_DESCRIPTION_FIELD).asText()
                ),
                entity.getAggregateVersion(),
                entity.getOccurredAt()
            );
        }
        throw new IllegalArgumentException("Unsupported event type: " + entity.getEventType());
    }
}

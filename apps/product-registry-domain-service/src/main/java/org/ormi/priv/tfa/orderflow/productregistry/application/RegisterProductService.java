package org.ormi.priv.tfa.orderflow.productregistry.application;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.EventLogEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.OutboxEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.EventLogRepository;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.OutboxRepository;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductRegistered;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductRepository;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.RegisterProductCommand;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * TODO: Complete Javadoc
 */

@ApplicationScoped
public class RegisterProductService {

    ProductRepository repository;
    EventLogRepository eventLog;
    OutboxRepository outbox;

    @Inject
    public RegisterProductService(
        ProductRepository repository,
        EventLogRepository eventLog,
        OutboxRepository outbox
    ) {
        this.repository = repository;
        this.eventLog = eventLog;
        this.outbox = outbox;
    }

    @Transactional
    public ProductId handle(RegisterProductCommand cmd) throws IllegalArgumentException {
        if (repository.existsBySkuId(cmd.skuId())) {
            throw new IllegalArgumentException(String.format("SKU already exists: %s", cmd.skuId()));
        }
        Product product = Product.create(
                cmd.name(),
                cmd.description(),
                cmd.skuId());
        // Save domain object
        repository.save(product);
        EventEnvelope<ProductRegistered> evt = EventEnvelope.with(new ProductRegistered(product.getId(), product.getSkuId(), cmd.name(), cmd.description()), product.getVersion());
        // Appends event to the log
        final EventLogEntity persistedEvent = eventLog.append(evt);
        // Publish outbox
        outbox.publish(OutboxEntity.Builder()
                .sourceEvent(persistedEvent)
                .build());
        return product.getId();
    }
}

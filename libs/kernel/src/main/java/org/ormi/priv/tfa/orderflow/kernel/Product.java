package org.ormi.priv.tfa.orderflow.kernel;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductDescriptionUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductNameUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductRetired;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductLifecycle;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * TODO: Complete Javadoc
 */

@Getter
public class Product {
    private static final Long INITIAL_VERSION = 1L;

    @NotNull
    private final ProductId id;
    @NotBlank
    private String name;
    @NotNull
    private String description;
    @NotNull
    private final SkuId skuId;
    @NotNull
    private ProductLifecycle status;
    @NotNull
    private Long version;

    private Product(
            ProductId id,
            String name,
            String description,
            SkuId skuId,
            ProductLifecycle status,
            Long version) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.skuId = skuId;
        this.status = status;
        this.version = version;
    }

    public static Product create(
            String name,
            String description,
            SkuId skuId) {
        return Builder()
                .id(ProductId.newId())
                .name(name)
                .description(description)
                .skuId(skuId)
                .status(ProductLifecycle.ACTIVE)
                .version(INITIAL_VERSION)
                .build();
    }

    public static ProductBuilder Builder() {
        return new ProductBuilder();
    }

    public EventEnvelope<ProductNameUpdated> updateName(String name) {
        if (!canUpdateDetails(this)) {
            throw new IllegalStateException("Cannot update a retired product");
        }
        final String oldName = this.name;
        this.name = name;
        this.version++;
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final var violations = validator.validate(this);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return EventEnvelope.with(new ProductNameUpdated(this.id, oldName, this.name), this.version);
    }

    public EventEnvelope<ProductDescriptionUpdated> updateDescription(String description) {
        if (!canUpdateDetails(this)) {
            throw new IllegalStateException("Cannot update the product");
        }
        final String oldDescription = this.description;
        this.description = description;
        this.version++;
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final var violations = validator.validate(this);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return EventEnvelope.with(new ProductDescriptionUpdated(this.id, oldDescription, this.description), this.version);
    }

    public EventEnvelope<ProductRetired> retire() {
        if (!canRetire()) {
            throw new IllegalStateException("Cannot retire the product");
        }
        this.status = ProductLifecycle.RETIRED;
        this.version++;
        return EventEnvelope.with(new ProductRetired(this.id), this.version);
    }

    private static boolean canUpdateDetails(Product p) {
        return p.status != ProductLifecycle.RETIRED;
    }

    private boolean canRetire() {
        if (this.status != ProductLifecycle.ACTIVE) {
            return false;
        }
        return true;
    }

    public static final class ProductBuilder {
        private ProductId id;
        private String name;
        private String description;
        private SkuId skuId;
        private ProductLifecycle status;
        private Long version;

        public ProductBuilder id(ProductId id) {
            this.id = id;
            return this;
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder skuId(SkuId skuId) {
            this.skuId = skuId;
            return this;
        }

        public ProductBuilder status(ProductLifecycle status) {
            this.status = status;
            return this;
        }

        public ProductBuilder version(Long version) {
            this.version = version;
            return this;
        }

        public Product build() throws ConstraintViolationException {
            Product product = new Product(id, name, description, skuId, status, version);
            final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            final var violations = validator.validate(product);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
            return product;
        }
    }
}

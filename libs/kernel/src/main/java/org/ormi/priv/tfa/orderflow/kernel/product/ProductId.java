package org.ormi.priv.tfa.orderflow.kernel.product;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * TODO: Complete Javadoc
 */

public record ProductId(@NotNull UUID value) {
    public static ProductId newId() {
        return new ProductId(UUID.randomUUID());
    }
}

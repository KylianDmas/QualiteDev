package org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write;

public record RegisterProductCommandDto(
        String name,
        String description,
        String skuId) {
}

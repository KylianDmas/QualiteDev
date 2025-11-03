package org.ormi.priv.tfa.orderflow.store.infra.api.dto;

public record SearchProductsDto(
        String sku,
        int page,
        int size) {
}

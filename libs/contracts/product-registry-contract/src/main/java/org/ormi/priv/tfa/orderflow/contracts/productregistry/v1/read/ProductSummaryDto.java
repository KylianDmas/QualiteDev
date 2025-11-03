package org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read;

public record ProductSummaryDto(
    String id,
    String skuId,
    String name,
    String status,
    Integer catalogs
) {
    
}

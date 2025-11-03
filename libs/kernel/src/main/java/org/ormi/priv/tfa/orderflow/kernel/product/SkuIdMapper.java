package org.ormi.priv.tfa.orderflow.kernel.product;

import org.mapstruct.Mapper;

import jakarta.enterprise.context.ApplicationScoped;

@Mapper(componentModel = "cdi")
@ApplicationScoped
public interface SkuIdMapper {
    default String map(SkuId id) { return id.value(); }
    default SkuId map(String sku) { return new SkuId(sku); }
}
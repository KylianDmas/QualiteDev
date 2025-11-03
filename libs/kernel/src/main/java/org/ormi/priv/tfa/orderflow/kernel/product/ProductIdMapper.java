package org.ormi.priv.tfa.orderflow.kernel.product;

import java.util.UUID;

import org.mapstruct.Mapper;

import jakarta.enterprise.context.ApplicationScoped;

@Mapper(componentModel = "cdi")
@ApplicationScoped
public interface ProductIdMapper {
    default UUID map(ProductId id) {
        return id.value();
    }

    default ProductId map(UUID id) {
        return new ProductId(id);
    }
}
package org.ormi.priv.tfa.orderflow.productregistry.infra.jpa;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;

/**
 * TODO: Complete Javadoc
 */

@Mapper(
    componentModel = "cdi",
    builder = @Builder(disableBuilder = false),
    uses = { ProductIdMapper.class, SkuIdMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ProductJpaMapper {

    public abstract Product toDomain(ProductEntity entity);

    public abstract void updateEntity(Product product, @MappingTarget ProductEntity entity);

    public abstract ProductEntity toEntity(Product product);
}

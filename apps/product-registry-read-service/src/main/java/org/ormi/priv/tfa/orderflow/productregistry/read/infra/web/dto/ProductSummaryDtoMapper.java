package org.ormi.priv.tfa.orderflow.productregistry.read.infra.web.dto;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductSummaryDto;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductSummary;

@Mapper(
    componentModel = "cdi",
    builder = @Builder(disableBuilder = false),
    uses = { ProductIdMapper.class, SkuIdMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductSummaryDtoMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "skuId", target = "skuId")
    @Mapping(source = "name", target = "name")
    @Mapping(expression = "java(productView.getStatus().name())", target = "status")
    @Mapping(source = "catalogs", target = "catalogs")
    public ProductSummaryDto toDto(ProductSummary productView);
}

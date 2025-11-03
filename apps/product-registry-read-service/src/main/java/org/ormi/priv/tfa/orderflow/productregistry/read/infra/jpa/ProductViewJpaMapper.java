package org.ormi.priv.tfa.orderflow.productregistry.read.infra.jpa;

import java.io.IOException;
import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView.ProductViewCatalogRef;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView.ProductViewEvent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: Complete Javadoc
 */

@Mapper(
    componentModel = "cdi",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {ProductIdMapper.class, SkuIdMapper.class, }
)
public interface ProductViewJpaMapper {

    @Mapping(target = "events", expression = "java(productViewEventListToJsonNode(productView.getEvents(), objectMapper))")
    @Mapping(target = "catalogs", expression = "java(productViewCatalogRefListToJsonNode(productView.getCatalogs(), objectMapper))")
    public ProductViewEntity toEntity(ProductView productView, @Context ObjectMapper objectMapper);

    @Mapping(target = "events", expression = "java(jsonNodeToProductViewEventList(entity.getEvents(), objectMapper))")
    @Mapping(target = "catalogs", expression = "java(jsonNodeToProductViewCatalogRefList(entity.getCatalogs(), objectMapper))")
    public ProductView toDomain(ProductViewEntity entity, @Context ObjectMapper objectMapper);

    @Mapping(target = "events", expression = "java(productViewEventListToJsonNode(productView.getEvents(), objectMapper))")
    @Mapping(target = "catalogs", expression = "java(productViewCatalogRefListToJsonNode(productView.getCatalogs(), objectMapper))")
    public void updateEntity(ProductView productView, @MappingTarget ProductViewEntity entity, @Context ObjectMapper objectMapper);

    default JsonNode productViewEventListToJsonNode(List<ProductViewEvent> events, @Context ObjectMapper om) {
        return om.valueToTree(events);
    }

    default JsonNode productViewCatalogRefListToJsonNode(List<ProductViewCatalogRef> catalogRefs, @Context ObjectMapper om) {
        return om.valueToTree(catalogRefs);
    }

    // === JSON deserialization helpers ===
    default List<ProductViewEvent> jsonNodeToProductViewEventList(JsonNode node, @Context ObjectMapper om) {
        try {
            return om.readValue(
                om.treeAsTokens(node),
                new TypeReference<List<ProductViewEvent>>() {}
            );
        } catch (IOException e) {
            throw new IllegalStateException("Failed to deserialize ProductViewEvent list", e);
        }
    }

    default List<ProductViewCatalogRef> jsonNodeToProductViewCatalogRefList(JsonNode node, @Context ObjectMapper om) {
        try {
            return om.readValue(
                om.treeAsTokens(node),
                new TypeReference<List<ProductViewCatalogRef>>() {}
            );
        } catch (IOException e) {
            throw new IllegalStateException("Failed to deserialize ProductViewCatalogRef list", e);
        }
    }
}

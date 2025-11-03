package org.ormi.priv.tfa.orderflow.productregistry.read.infra.api;

import java.util.UUID;

import org.jboss.resteasy.reactive.RestResponse;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.PaginatedProductListDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductViewDto;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductSummary;
import org.ormi.priv.tfa.orderflow.productregistry.read.application.ReadProductService;
import org.ormi.priv.tfa.orderflow.productregistry.read.application.ReadProductService.SearchPaginatedResult;
import org.ormi.priv.tfa.orderflow.productregistry.read.infra.web.dto.ProductSummaryDtoMapper;
import org.ormi.priv.tfa.orderflow.productregistry.read.infra.web.dto.ProductViewDtoMapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

/**
 * TODO: Complete Javadoc
 */

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductRegistryQueryResource {

    private final ReadProductService readProductService;
    private final ProductViewDtoMapper productViewDtoMapper;
    private final ProductSummaryDtoMapper productSummaryDtoMapper;
    private final ProductIdMapper productIdMapper;

    @Inject
    public ProductRegistryQueryResource(
            ReadProductService readProductService,
            ProductViewDtoMapper productViewDtoMapper,
            ProductSummaryDtoMapper productSummaryDtoMapper,
            ProductIdMapper productIdMapper) {
        this.readProductService = readProductService;
        this.productViewDtoMapper = productViewDtoMapper;
        this.productSummaryDtoMapper = productSummaryDtoMapper;
        this.productIdMapper = productIdMapper;
    }

    @GET
    public RestResponse<PaginatedProductListDto> searchProducts(
            @QueryParam("sku") @DefaultValue("") String sku,
            @QueryParam("page") int page,
            @QueryParam("size") int size) {
        // TODO: Validation [Exercice 5]
        final SearchPaginatedResult result = readProductService.searchProducts(sku, page, size);
        final PaginatedProductListDto list = new PaginatedProductListDto(result.page().stream()
                .map(view -> ProductSummary.Builder()
                        .id(view.getId())
                        .skuId(view.getSkuId())
                        .name(view.getName())
                        .status(view.getStatus())
                        .catalogs(view.getCatalogs().size())
                        .build())
                .map(productSummaryDtoMapper::toDto)
                .toList(), page, size, result.total());
        return RestResponse.ok(list);
    }

    @GET
    @Path("/{id}")
    public RestResponse<ProductViewDto> getProductById(@PathParam("id") String id) {
        // TODO: Validation [Exercice 5]
        final var product = readProductService.findById(productIdMapper.map(UUID.fromString(id)));
        if (product.isEmpty()) {
            return RestResponse.status(RestResponse.Status.NOT_FOUND);
        }
        return RestResponse.ok(productViewDtoMapper.toDto(product.get()));
    }
}
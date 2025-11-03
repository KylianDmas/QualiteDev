package org.ormi.priv.tfa.orderflow.store.infra.api;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestResponse;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.PaginatedProductListDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductViewDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.RegisterProductCommandDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.UpdateProductDescriptionParamsDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.UpdateProductNameParamsDto;
import org.ormi.priv.tfa.orderflow.store.infra.api.dto.RetireProductDto;
import org.ormi.priv.tfa.orderflow.store.infra.api.dto.SearchProductsDto;
import org.ormi.priv.tfa.orderflow.store.infra.api.dto.UpdateProductDto;
import org.ormi.priv.tfa.orderflow.store.infra.api.dto.ViewProductDto;
import org.ormi.priv.tfa.orderflow.store.infra.rest.client.ProductRegistryDomainService;
import org.ormi.priv.tfa.orderflow.store.infra.rest.client.ProductRegistryService;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

/**
 * TODO: Complete Javadoc
 */

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductRpcResource {

    @Inject
    @RestClient
    private ProductRegistryService productRegistryService;
    @Inject
    @RestClient
    private ProductRegistryDomainService productRegistryDomainService;

    @POST
    @Path("/registerProduct")
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<Void> registerProduct(RegisterProductCommandDto product) {
        final var res = productRegistryDomainService.registerProduct(product);
        if (res.getStatus() == Status.CREATED.getStatusCode()) {
            return RestResponse.ok();
        } else {
            return RestResponse.status(Status.BAD_REQUEST);
        }
    }

    @POST
    @Path("/updateProduct")
    @Consumes(MediaType.APPLICATION_JSON)
    @Blocking
    public Uni<RestResponse<Void>> updateProduct(UpdateProductDto update) {
        if (update.id() == null || update.id().isEmpty()) {
            return Uni.createFrom().item(RestResponse.status(Status.BAD_REQUEST));
        }
        if (update.operations() == null || update.operations().length == 0) {
            return Uni.createFrom().item(RestResponse.status(Status.BAD_REQUEST));
        }

        List<Uni<RestResponse<?>>> ops = new ArrayList<>();

        for (var op: update.operations()) {
            switch (op.type()) {
                case UPDATE_NAME -> {
                    var nameOp = (UpdateProductDto.UpdateNameOperation) op;
                    ops.add(Uni.createFrom().item(
                        productRegistryDomainService.updateProductName(update.id(),
                            new UpdateProductNameParamsDto(nameOp.payload().name()))
                    ));
                }
                case UPDATE_DESCRIPTION -> {
                    var descOp = (UpdateProductDto.UpdateDescriptionOperation) op;
                    ops.add(Uni.createFrom().item(
                        productRegistryDomainService.updateProductDescription(update.id(),
                            new UpdateProductDescriptionParamsDto(descOp.payload().description()))
                    ));
                }
            }
        }

        if (ops.isEmpty()) {
            return Uni.createFrom().item(RestResponse.status(Status.NOT_MODIFIED));
        }

        return Uni.combine().all().unis(ops).with(results -> {
            boolean allOk = results.stream()
                .map(r -> ((RestResponse<?>) r).getStatus())
                .allMatch(status -> status == Status.NO_CONTENT.getStatusCode());
            if (allOk) {
                return RestResponse.ok();
            } else {
                return RestResponse.status(Status.INTERNAL_SERVER_ERROR);
            }
        });
    }

    @POST
    @Path("/retireProduct")
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<Void> retireProduct(RetireProductDto retire) {
        if (retire.id() == null || retire.id().isEmpty()) {
            return RestResponse.status(Status.BAD_REQUEST);
        }
        final var res = productRegistryDomainService.retireProduct(retire.id());
        if (res.getStatus() == Status.NO_CONTENT.getStatusCode()) {
            return RestResponse.ok();
        } else {
            return RestResponse.status(Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/viewProduct")
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<ProductViewDto> viewProduct(ViewProductDto view) {
        if (view.id() == null || view.id().isEmpty()) {
            return RestResponse.status(Status.BAD_REQUEST);
        }
        final var res = productRegistryService.getProductById(view.id());
        if (res.getStatus() == Status.OK.getStatusCode()) {
            return RestResponse.ok(res.getEntity());
        } else {
            return RestResponse.status(Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/searchProducts")
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<PaginatedProductListDto> searchProducts(SearchProductsDto search) {
        final var res = productRegistryService.searchProducts(search.sku(), search.page(), search.size());
        if (res.getStatus() == Status.OK.getStatusCode()) {
            return RestResponse.ok(res.getEntity());
        } else {
            return RestResponse.status(Status.INTERNAL_SERVER_ERROR);
        }
    }

    // TODO: implement [Exercice 5]
    // @GET
    // @Path("/{id}/streamProductEvents")
    // @Produces(MediaType.SERVER_SENT_EVENTS)
    // @RestStreamElementType(MediaType.APPLICATION_JSON)
    // public Multi<ProductStreamElementDto> streamProductEventsById(@QueryParam("id") String id) {
    //     throw new UnsupportedOperationException("TODO: implement [Exercice 5]");
    // }
}
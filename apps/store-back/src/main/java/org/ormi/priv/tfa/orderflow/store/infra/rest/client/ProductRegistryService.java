package org.ormi.priv.tfa.orderflow.store.infra.rest.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.PaginatedProductListDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductViewDto;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

/**
 * TODO: Complete Javadoc
 */

@ApplicationScoped
@Path("/products")
@RegisterRestClient(configKey = "product-registry-read-api")
public interface ProductRegistryService {

    @GET
    RestResponse<PaginatedProductListDto> searchProducts(
            @QueryParam("sku") String sku,
            @QueryParam("page") int page,
            @QueryParam("size") int size);

    @GET
    @Path("/{id}")
    RestResponse<ProductViewDto> getProductById(@PathParam("id") String id);

    // TODO: implement [Exercice 5] (streamProductEventsByProductId) (Utiliser Multi de SmallRye Mutiny)
}

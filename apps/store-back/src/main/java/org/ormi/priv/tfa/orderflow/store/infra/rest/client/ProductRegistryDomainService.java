package org.ormi.priv.tfa.orderflow.store.infra.rest.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.RegisterProductCommandDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.UpdateProductDescriptionParamsDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.UpdateProductNameParamsDto;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

/**
 * TODO: Complete Javadoc
 */

@ApplicationScoped
@Path("/products")
@RegisterRestClient(configKey = "product-registry-api")
public interface ProductRegistryDomainService {
    
    @POST
    RestResponse<Void> registerProduct(RegisterProductCommandDto cmd);

    @DELETE
    @Path("/{id}")
    RestResponse<Void> retireProduct(@PathParam("id") String productId);

    @PATCH
    @Path("/{id}/name")
    RestResponse<Void> updateProductName(@PathParam("id") String productId, UpdateProductNameParamsDto params);

    @PATCH
    @Path("/{id}/description")
    RestResponse<Void> updateProductDescription(@PathParam("id") String productId, UpdateProductDescriptionParamsDto params);
}

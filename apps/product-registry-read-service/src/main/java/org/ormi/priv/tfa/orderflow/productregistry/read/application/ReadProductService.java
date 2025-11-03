package org.ormi.priv.tfa.orderflow.productregistry.read.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductStreamElementDto;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductViewRepository;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView;

import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * TODO: Complete Javadoc
 */

@ApplicationScoped
public class ReadProductService {

    private final ProductViewRepository repository;
    private final ProductEventBroadcaster productEventBroadcaster;

    @Inject
    public ReadProductService(
        ProductViewRepository repository,
        ProductEventBroadcaster productEventBroadcaster) {
        this.repository = repository;
        this.productEventBroadcaster = productEventBroadcaster;
    }

    public Optional<ProductView> findById(ProductId productId) {
        return repository.findById(productId);
    }

    public SearchPaginatedResult searchProducts(String skuIdPattern, int page, int size) {
        return new SearchPaginatedResult(
                repository.searchPaginatedViewsOrderBySkuId(skuIdPattern, page, size),
                repository.countPaginatedViewsBySkuIdPattern(skuIdPattern));
    }

    public Multi<ProductStreamElementDto> streamProductEvents(ProductId productId) {
        return productEventBroadcaster.stream()
                .select().where(e -> e.productId().equals(productId.value().toString()));
    }

    public Multi<ProductStreamElementDto> streamProductListEvents(String skuIdPattern, int page, int size) {
        final List<ProductView> products = searchProducts(skuIdPattern, page, size).page();
        final List<UUID> productIds = products.stream()
                .map(p -> p.getId().value())
                .toList();
        return productEventBroadcaster.stream()
                .select().where(e -> productIds.contains(UUID.fromString(e.productId())));
    }

    public record SearchPaginatedResult(List<ProductView> page, long total) {
    }
}

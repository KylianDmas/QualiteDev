package org.ormi.priv.tfa.orderflow.productregistry.read.infra.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductViewRepository;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JpaProductViewRepository implements PanacheRepositoryBase<ProductViewEntity, UUID>, ProductViewRepository {

    private final ProductViewJpaMapper mapper;
    private final ObjectMapper objectMapper;

    public JpaProductViewRepository(ProductViewJpaMapper mapper, ObjectMapper objectMapper) {
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(ProductView productView) {
        findByIdOptional(productView.getId().value())
                .ifPresentOrElse(e -> {
                    mapper.updateEntity(productView, e, objectMapper);
                }, () -> {
                    ProductViewEntity newEntity = mapper.toEntity(productView, objectMapper);
                    getEntityManager().merge(newEntity);
                });
    }

    @Override
    public Optional<ProductView> findById(ProductId id) {
        return findByIdOptional(id.value())
                .map(e -> mapper.toDomain(e, objectMapper));
    }

    @Override
    public Optional<ProductView> findBySkuId(SkuId skuId) {
        return find("skuId", skuId.value())
                .firstResultOptional()
                .map(e -> mapper.toDomain(e, objectMapper));
    }

    @Override
    public List<ProductView> searchPaginatedViewsOrderBySkuId(String skuIdPattern, int page, int size) {
        return find("skuId LIKE ?1 ORDER BY skuId", "%" + skuIdPattern + "%")
                .page(page - 1, size)
                .list()
                .stream()
                .map(e -> mapper.toDomain(e, objectMapper))
                .toList();
    }

    @Override
    public long countPaginatedViewsBySkuIdPattern(String skuIdPattern) {
        return count("skuId LIKE ?1", "%" + skuIdPattern + "%");
    }

}

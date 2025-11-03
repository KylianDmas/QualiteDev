package org.ormi.priv.tfa.orderflow.productregistry.infra.jpa;

import java.util.Optional;
import java.util.UUID;

import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * TODO: Complete Javadoc
 */

@ApplicationScoped
public class JpaProductRepository implements PanacheRepositoryBase<ProductEntity, UUID>, ProductRepository {

    ProductJpaMapper mapper;
    ProductIdMapper productIdMapper;    
    SkuIdMapper skuIdMapper;

    @Inject
    public JpaProductRepository(ProductJpaMapper mapper, ProductIdMapper productIdMapper, SkuIdMapper skuIdMapper) {
        this.mapper = mapper;
        this.productIdMapper = productIdMapper;
        this.skuIdMapper = skuIdMapper;
    }

    @Override
    @Transactional
    public void save(Product product) {
        findByIdOptional(productIdMapper.map(product.getId()))
                .ifPresentOrElse(e -> {
                    mapper.updateEntity(product, e);
                }, () -> {
                    ProductEntity newEntity = mapper.toEntity(product);
                    getEntityManager().merge(newEntity);
                });
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return findByIdOptional(productIdMapper.map(id))
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsBySkuId(SkuId skuId) {
        return count("skuId", skuIdMapper.map(skuId)) > 0;
    }
}

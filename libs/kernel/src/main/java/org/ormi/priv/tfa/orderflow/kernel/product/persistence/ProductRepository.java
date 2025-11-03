package org.ormi.priv.tfa.orderflow.kernel.product.persistence;

import java.util.Optional;

import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;

/**
 * TODO: Complete Javadoc
 */

public interface ProductRepository {
    void save(Product product);
    Optional<Product> findById(ProductId id);
    boolean existsBySkuId(SkuId skuId);
}

package org.ormi.priv.tfa.orderflow.productregistry.read.application;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;

/**
 * TODO: Complete Javadoc
 */

public sealed interface ProductQuery {
    public record GetProductByIdQuery(ProductId productId) implements ProductQuery {
    }

    public record ListProductQuery(int page, int size) implements ProductQuery {
    }

    public record ListProductBySkuIdPatternQuery(String skuIdPattern, int page, int size) implements ProductQuery {
    }
}

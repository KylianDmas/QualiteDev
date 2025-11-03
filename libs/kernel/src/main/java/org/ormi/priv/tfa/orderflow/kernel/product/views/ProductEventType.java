package org.ormi.priv.tfa.orderflow.kernel.product.views;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductDescriptionUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductNameUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductRegistered;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductRetired;

public enum ProductEventType {
    PRODUCT_REGISTERED(ProductRegistered.class.getSimpleName()),
    PRODUCT_NAME_UPDATED(ProductNameUpdated.class.getSimpleName()),
    PRODUCT_DESCRIPTION_UPDATED(ProductDescriptionUpdated.class.getSimpleName()),
    PRODUCT_RETIRED(ProductRetired.class.getSimpleName());

    private final String value;

    ProductEventType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}

package org.ormi.priv.tfa.orderflow.kernel.common;

import org.ormi.priv.tfa.orderflow.kernel.Product;

public enum AggregateType {
    PRODUCT(Product.class.getSimpleName());

    private final String value;

    AggregateType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}

package org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read;

import java.time.Instant;

/**
 * TODO: Complete Javadoc
 */

public record ProductStreamElementDto(
    String type,
    String productId,
    Instant occuredAt
) {
}

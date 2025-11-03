package org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read;

import java.util.List;

public record PaginatedProductListDto(
    List<ProductSummaryDto> products,
    int page,
    int pageSize,
    long totalElements
) {
}

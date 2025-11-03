package org.ormi.priv.tfa.orderflow.productregistry.infra.jpa;

import java.util.UUID;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductLifecycle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * TODO: Complete Javadoc
 */

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(
    schema = "domain",
    name = "products",
    indexes = {
        @Index(name = "ux_products_sku", columnList = "sku", unique = true)
    })
public class ProductEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;
    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;
    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;
    @Column(name = "sku_id", nullable = false, updatable = false, length = 9, unique = true, columnDefinition = "varchar(9)")
    private String skuId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "text")
    private ProductLifecycle status;
    @Column(name = "version", nullable = false, columnDefinition = "bigint")
    private Long version;
}

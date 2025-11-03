package org.ormi.priv.tfa.orderflow.productregistry.read.infra.jpa;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductLifecycle;

import com.fasterxml.jackson.databind.JsonNode;

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
    schema = "read_product_registry",
    name = "product_view",
    indexes = {
        @Index(name = "idx_prdview_sku", columnList = "sku_id")
    })
public class ProductViewEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;
    @Column(name = "_version", nullable = false, columnDefinition = "bigint")
    private Long version;
    @Column(name = "sku_id", nullable = false, length = 9, unique = true, columnDefinition = "varchar(9)")
    private String skuId;
    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;
    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "text")
    private ProductLifecycle status;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "events", nullable = false, columnDefinition = "jsonb")
    private JsonNode events;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "catalogs", nullable = false, columnDefinition = "jsonb")
    private JsonNode catalogs;
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamptz")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamptz")
    private Instant updatedAt;
}

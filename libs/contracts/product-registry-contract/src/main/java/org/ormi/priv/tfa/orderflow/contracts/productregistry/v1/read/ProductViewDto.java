package org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * TODO: Complete Javadoc
 */

public record ProductViewDto(
        String id,
        String skuId,
        String name,
        String status,
        String description,
        List<ProductViewDtoCatalog> catalogs,
        List<ProductViewDtoEvent> events,
        String createdAt,
        String updatedAt) {

    public static record ProductViewDtoCatalog(
            String id,
            String name) {
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = ProductViewEventDtoPayload.ProductRegisteredPayloadDto.class, name = "ProductRegistered"),
        @JsonSubTypes.Type(value = ProductViewEventDtoPayload.ProductNameUpdatedPayloadDto.class, name = "ProductNameUpdated"),
        @JsonSubTypes.Type(value = ProductViewEventDtoPayload.ProductDescriptionUpdatedPayloadDto.class, name = "ProductDescriptionUpdated"),
        @JsonSubTypes.Type(value = ProductViewEventDtoPayload.ProductRetiredPayloadDto.class, name = "ProductRetired")
    })
    public sealed interface ProductViewEventDtoPayload {
        public record ProductRegisteredPayloadDto(
                String skuId,
                String name,
                String description) implements ProductViewEventDtoPayload {
        }
        public record ProductNameUpdatedPayloadDto(
                String oldName,
                String newName) implements ProductViewEventDtoPayload {
        }
        public record ProductDescriptionUpdatedPayloadDto(
                String oldDescription,
                String newDescription) implements ProductViewEventDtoPayload {
        }
        public record ProductRetiredPayloadDto() implements ProductViewEventDtoPayload {
        }
    }

    public static record ProductViewDtoEvent(
            ProductViewDtoEventType type,
            String timestamp,
            Long sequence,
            ProductViewEventDtoPayload payload) {
    }

    public static enum ProductViewDtoEventType {
        REGISTERED("ProductRegistered"),
        NAME_UPDATED("ProductNameUpdated"),
        DESCRIPTION_UPDATED("ProductDescriptionUpdated"),
        RETIRED("ProductRetired");

        private final String value;

        ProductViewDtoEventType(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @JsonCreator
        public static ProductViewDtoEventType fromValue(String value) {
            for (ProductViewDtoEventType type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown " + ProductViewDtoEventType.class.getSimpleName() + ": " + value);
        }
    }
}

package org.ormi.priv.tfa.orderflow.productregistry.read.projection;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.ormi.priv.tfa.orderflow.cqrs.Projector;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1Envelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1Envelope.ProductDescriptionUpdatedEnvelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1Envelope.ProductNameUpdatedEnvelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1Envelope.ProductRegisteredEnvelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1Envelope.ProductRetiredEnvelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductLifecycle;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductEventType;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView.ProductViewEvent;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * TODO: Complete Javadoc
 */
@ApplicationScoped
public class ProductViewProjector implements Projector<ProductView, ProductEventV1Envelope<?>> {

	@Override
	public ProjectionResult<ProductView> project(Optional<ProductView> current, ProductEventV1Envelope<?> ev) {
		return switch (ev) {
			case ProductRegisteredEnvelope pre -> handleProjection(current, pre);
			case ProductRetiredEnvelope pre -> handleProjection(current, pre);
			case ProductNameUpdatedEnvelope pre -> handleProjection(current, pre);
			case ProductDescriptionUpdatedEnvelope pre -> handleProjection(current, pre);
			default -> ProjectionResult.failed("Unimplemented event type");
		};
	}

	private ProjectionResult<ProductView> handleProjection(Optional<ProductView> current,
			ProductRegisteredEnvelope ev) {
		if (current.isPresent() && current.get().getStatus() == ProductLifecycle.ACTIVE) {
			return ProjectionResult.failed("Product already exists and is active");
		}
		ProductView newView = ProductView.Builder()
				.id(new ProductId(ev.event().productId().value()))
				.version(ev.sequence())
				.skuId(new SkuId(ev.event().payload().skuId()))
				.name(ev.event().payload().name())
				.description(ev.event().payload().description())
				.status(ProductLifecycle.ACTIVE)
				.events(List.of(
						new ProductViewEvent(
								ProductEventType.PRODUCT_REGISTERED,
								ev.timestamp(),
								ev.sequence(),
								ev.event().payload())))
				.catalogs(Collections.emptyList())
				.createdAt(ev.timestamp())
				.updatedAt(ev.timestamp())
				.build();
		return ProjectionResult.projected(newView);
	}

	private ProjectionResult<ProductView> handleProjection(Optional<ProductView> current, ProductRetiredEnvelope ev) {
		if (current.isEmpty() || current.get().getStatus() != ProductLifecycle.ACTIVE) {
			return ProjectionResult.failed("Already retired or never existed");
		}
		if (ev.sequence() <= current.get().getVersion()) {
			return ProjectionResult.noOp("Stale retirement ignored");
		}
		ProductView newView = ProductView.Builder()
				.with(current.get())
				.version(ev.sequence())
				.status(ProductLifecycle.RETIRED)
				.events(mergeEvents(current.get().getEvents(),
						new ProductViewEvent(
							ProductEventType.PRODUCT_RETIRED,
							ev.timestamp(),
							ev.sequence(),
							ev.event().payload())))
				.build();
		return ProjectionResult.projected(newView);
	}

	private ProjectionResult<ProductView> handleProjection(Optional<ProductView> current,
			ProductNameUpdatedEnvelope ev) {
		if (current.isEmpty() || current.get().getStatus() != ProductLifecycle.ACTIVE) {
			return ProjectionResult.failed("Cannot update name of non-existent or retired product");
		}
		if (ev.sequence() <= current.get().getVersion()) {
			return ProjectionResult.noOp("Stale name update ignored");
		}
		ProductView newView = ProductView.Builder()
				.with(current.get())
				.version(ev.sequence())
				.name(ev.event().payload().newName())
				.events(mergeEvents(current.get().getEvents(),
						new ProductViewEvent(
							ProductEventType.PRODUCT_NAME_UPDATED,
							ev.timestamp(),
							ev.sequence(),
							ev.event().payload())))
				.build();
		return ProjectionResult.projected(newView);
	}

	private ProjectionResult<ProductView> handleProjection(Optional<ProductView> current,
			ProductDescriptionUpdatedEnvelope ev) {
		if (current.isEmpty() || current.get().getStatus() != ProductLifecycle.ACTIVE) {
			return ProjectionResult.failed("Cannot update description of non-existent or retired product");
		}
		if (ev.sequence() <= current.get().getVersion()) {
			return ProjectionResult.noOp("Stale description update ignored");
		}
		ProductView newView = ProductView.Builder()
				.with(current.get())
				.version(ev.sequence())
				.description(ev.event().payload().newDescription())
				.events(mergeEvents(current.get().getEvents(),
						new ProductViewEvent(
							ProductEventType.PRODUCT_DESCRIPTION_UPDATED,
							ev.timestamp(),
							ev.sequence(),
							ev.event().payload())))
				.build();
		return ProjectionResult.projected(newView);
	}

	private static List<ProductViewEvent> mergeEvents(List<ProductViewEvent> existingEvents,
			ProductViewEvent newEvent) {
		return Stream.concat(existingEvents.stream(),
				Stream.of(newEvent))
				.sorted(Comparator.comparingLong(ProductViewEvent::getSequence))
				.toList();
	}
}

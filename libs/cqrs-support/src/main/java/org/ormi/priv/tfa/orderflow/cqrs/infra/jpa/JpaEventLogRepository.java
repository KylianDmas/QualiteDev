package org.ormi.priv.tfa.orderflow.cqrs.infra.jpa;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.EventLogRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.arc.DefaultBean;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * TODO: Complete Javadoc
 */

@ApplicationScoped
@DefaultBean
public class JpaEventLogRepository implements PanacheRepository<EventLogEntity>, EventLogRepository {

    private final EventLogJpaMapper mapper;
    private final ObjectMapper objectMapper;

    @Inject
    public JpaEventLogRepository(EventLogJpaMapper mapper, ObjectMapper objectMapper) {
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

	@Override
    @Transactional
	public EventLogEntity append(EventEnvelope<?> eventLog) {
		EventLogEntity entity = mapper.toEntity(eventLog, objectMapper);
		persist(entity);
		return entity;
	}
}

package com.rental_pg_backend.application.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rental_pg_backend.application.entities.Application;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    boolean existsByTenantIdAndPropertyId(UUID tenantId, UUID propertyId);

    List<Application> findByTenantId(UUID tenantId);
}

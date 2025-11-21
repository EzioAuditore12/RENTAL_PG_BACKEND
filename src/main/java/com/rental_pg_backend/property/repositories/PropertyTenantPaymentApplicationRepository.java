package com.rental_pg_backend.property.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rental_pg_backend.property.entities.PropertyTenantPaymentApplication;

public interface PropertyTenantPaymentApplicationRepository
                extends JpaRepository<PropertyTenantPaymentApplication, Long> {

        boolean existsByApplication_Id(UUID applicationId);

}

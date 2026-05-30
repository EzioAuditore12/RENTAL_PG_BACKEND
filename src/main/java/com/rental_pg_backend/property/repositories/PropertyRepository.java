package com.rental_pg_backend.property.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rental_pg_backend.property.entities.Property;

public interface PropertyRepository extends JpaRepository<Property, UUID>, JpaSpecificationExecutor<Property> {

    List<Property> findAllByManagerId(UUID managerId);
    Page<Property> findAllByManagerId(UUID managerId, Pageable pageable);
    long countByManagerId(UUID managerId);
}

package com.rental_pg_backend.application.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rental_pg_backend.application.entities.Lease;

public interface LeaseRepository extends JpaRepository<Lease, UUID> {

    boolean existsByApplication_Id(UUID applicationId);

}

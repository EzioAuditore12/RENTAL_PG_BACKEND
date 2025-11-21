package com.rental_pg_backend.manager.repositories;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rental_pg_backend.manager.dto.ManagerIdPasswordDto;
import com.rental_pg_backend.manager.entities.Manager;

public interface ManagerRepository extends JpaRepository<Manager, UUID> {

    @Query("SELECT new com.rental_pg_backend.manager.dto.ManagerIdPasswordDto(t.id, t.password) FROM Manager t WHERE t.id = :id")
    ManagerIdPasswordDto findIdAndPasswordById(@Param("id") UUID id);

    @Query("SELECT t.id FROM Manager t WHERE t.id = :id")
    UUID findIdById(@Param("id") UUID id);

    Optional<Manager> findByEmail(String email);

    @Query("SELECT p.id FROM Property p WHERE p.manager.id = :managerId")
    Set<UUID> findManagedPropertyIdsByManagerId(@Param("managerId") UUID managerId);
}

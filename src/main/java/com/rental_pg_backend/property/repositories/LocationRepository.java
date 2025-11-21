package com.rental_pg_backend.property.repositories;

import java.util.Optional;
import java.util.UUID;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rental_pg_backend.property.entities.Location;

public interface LocationRepository extends JpaRepository<Location, UUID> {

    Optional<Location> findByCoordinates(Point coordinates);

}

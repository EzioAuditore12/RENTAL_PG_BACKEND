package com.rental_pg_backend.property.entities;

import java.util.UUID;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        indexes = {
            @Index(name = "idx_location_city", columnList = "city"),
                @Index(name = "idx_location_state", columnList = "state"),
                @Index(name = "idx_location_country", columnList = "country"),
                @Index(name = "idx_location_postal_code", columnList = "postalCode")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {

    @Id
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Property property;

    @Column(length = 200, nullable = false)
    private String address;

    @Column(length = 100, nullable = false)
    private String city;

    @Column(length = 100, nullable = false)
    private String state;

    @Column(length = 100, nullable = false)
    private String country;

    @Column(length = 20, nullable = false)
    private String postalCode;

    @Column(nullable = false, columnDefinition = "geometry(Point,4326)")
    private Point coordinates;

}

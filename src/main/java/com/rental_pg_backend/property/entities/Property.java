package com.rental_pg_backend.property.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

import com.rental_pg_backend.application.entities.Application;
import com.rental_pg_backend.manager.entities.Manager;
import com.rental_pg_backend.property.enums.AmenityType;
import com.rental_pg_backend.property.enums.HighlightType;
import com.rental_pg_backend.property.enums.PropertyType;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_property_name", columnList = "name"),
                @Index(name = "idx_property_price", columnList = "pricePerMonth"),
                @Index(name = "idx_property_type", columnList = "propertyType"),
                @Index(name = "idx_property_posted_date", columnList = "postedDate"),
                @Index(name = "idx_property_manager", columnList = "property_manager_id"),
                @Index(name = "idx_property_pet_allowed", columnList = "petAllowed"),
                @Index(name = "idx_property_parking", columnList = "parkingIncluded")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 250, nullable = false)
    private String description;

    @Builder.Default
    private Double pricePerMonth = 0.0;

    @Builder.Default
    private Double securityDeposit = 0.0;

    @Builder.Default
    private List<String> photoUrls = new ArrayList<>();

    private List<AmenityType> amenities;

    private List<HighlightType> highlights;

    private PropertyType propertyType;

    @Builder.Default
    private boolean petAllowed = false;

    @Builder.Default
    private boolean parkingIncluded = false;

    @Builder.Default
    private Integer beds = 0;

    @Builder.Default
    private Integer baths = 0;

    @Builder.Default
    private Double squareFeet = 0.0;

    @Builder.Default
    private Double averageRatings = 0.0;

    @Builder.Default
    private Double numberOfRatings = 0.0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime postedDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_manager_id")
    private Manager manager;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> applications;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PropertyTenantPaymentApplication> propertyTenantPaymentApplications;

}

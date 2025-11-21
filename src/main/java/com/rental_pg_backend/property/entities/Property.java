package com.rental_pg_backend.property.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.rental_pg_backend.application.entities.Application;
import com.rental_pg_backend.manager.entities.Manager;
import com.rental_pg_backend.property.enums.AmenityType;
import com.rental_pg_backend.property.enums.HighlightType;
import com.rental_pg_backend.property.enums.PropertyType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 250, nullable = false)
    private String description;

    @Column(nullable = false)
    private Double pricePerMonth = 0.0;

    @Column(nullable = false)
    private Double securityDeposit = 0.0;

    @Column(nullable = false)
    private List<String> photoUrls = new ArrayList<>();

    @Column(nullable = false)
    private List<AmenityType> amenities;

    @Column(nullable = false)
    private List<HighlightType> highlights;

    private PropertyType propertyType;

    @Column(nullable = false)
    private boolean petAllowed = false;

    @Column(nullable = false)
    private boolean parkingIncluded = false;

    @Column(nullable = false)
    private Integer beds = 0;

    @Column(nullable = false)
    private Integer baths = 0;

    @Column(nullable = false)
    private Double squareFeet = 0.0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime postedDate;

    @Column(nullable = false)
    private Double averageRatings = 0.0;

    @Column(nullable = false)
    private Double numberOfRatings = 0.0;

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

package com.rental_pg_backend.manager.entities;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.rental_pg_backend.property.entities.Property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        indexes = {
                @Index(name = "idx_manager_created_at", columnList = "createdAt"),
                @Index(name = "idx_manager_updated_at", columnList = "updatedAt")
        }
)
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 240, nullable = false, unique = true)
    private String email;

    @Column(columnDefinition = "text", nullable = true)
    private String avatar;

    @Column(columnDefinition = "text", nullable = false)
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Property> managedProperties;

}

package com.rental_pg_backend.tenant.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantPublicDto {

    private UUID id;

    private String name;

    private String email;

    private String avatar;

    private LocalDateTime createdAt;

    private Set<UUID> applicationIds;

    private Set<UUID> paymentIds;

}

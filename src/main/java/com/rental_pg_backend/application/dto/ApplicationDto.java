package com.rental_pg_backend.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.rental_pg_backend.application.enums.ApplicationStatusType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDto {

    private UUID id;

    private LocalDateTime startDate;

    private ApplicationStatusType status;

    private UUID propertyId;

    private UUID tenantId;

    private UUID leaseId;

    private Long propertyTenantPaymentApplicationId;

}

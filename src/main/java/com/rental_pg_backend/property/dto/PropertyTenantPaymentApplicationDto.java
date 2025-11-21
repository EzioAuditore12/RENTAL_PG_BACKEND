package com.rental_pg_backend.property.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyTenantPaymentApplicationDto {

    private Long id;

    private UUID tenantId;

    private UUID propertyId;

    private UUID paymentId;

    private UUID applicationId;
}

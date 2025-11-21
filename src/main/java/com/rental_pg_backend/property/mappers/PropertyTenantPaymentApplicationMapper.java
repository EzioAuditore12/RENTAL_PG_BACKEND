package com.rental_pg_backend.property.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rental_pg_backend.property.dto.PropertyTenantPaymentApplicationDto;
import com.rental_pg_backend.property.entities.PropertyTenantPaymentApplication;

@Mapper(componentModel = "spring")
public interface PropertyTenantPaymentApplicationMapper {

    @Mapping(target = "applicationId", source = "application.id")
    @Mapping(target = "tenantId", source = "tenant.id")
    @Mapping(target = "paymentId", source = "payment.id")
    @Mapping(target = "propertyId", source = "property.id")
    PropertyTenantPaymentApplicationDto toDto(PropertyTenantPaymentApplication propertyTenantPaymentApplication);
}

package com.rental_pg_backend.application;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rental_pg_backend.application.dto.ApplicationDto;
import com.rental_pg_backend.application.entities.Application;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    @Mapping(target = "leaseId", source = "lease.id")
    @Mapping(target = "propertyId", source = "property.id")
    @Mapping(target = "propertyTenantPaymentApplicationId", source = "propertyTenantPaymentApplication.id")
    @Mapping(target = "tenantId", source = "tenant.id")
    ApplicationDto toDto(Application application);

}

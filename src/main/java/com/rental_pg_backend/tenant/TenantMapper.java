package com.rental_pg_backend.tenant;

import java.util.Set;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.rental_pg_backend.application.entities.Application;
import com.rental_pg_backend.common.utils.MapUtils;
import com.rental_pg_backend.payment.entities.Payment;
import com.rental_pg_backend.property.entities.PropertyTenantPaymentApplication;
import com.rental_pg_backend.tenant.dto.CreateTenantDto;
import com.rental_pg_backend.tenant.dto.TenantDto;
import com.rental_pg_backend.tenant.dto.TenantPublicDto;
import com.rental_pg_backend.tenant.entities.Tenant;

@Mapper(componentModel = "spring")
public interface TenantMapper {

    @Mapping(source = "applications", target = "applicationIds", qualifiedByName = "applicationToUuids")
    @Mapping(source = "payments", target = "paymentIds", qualifiedByName = "paymentToUuids")
    @Mapping(source = "propertyTenantPaymentApplications", target = "propertyTenantPaymentApplicationIds", qualifiedByName = "propertyTenantPaymentApplicationsToIds")
    TenantDto toDto(Tenant tenant);

    TenantPublicDto toPublicDto(TenantDto tenantDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "payments", ignore = true)
    @Mapping(target = "propertyTenantPaymentApplications", ignore = true)
    Tenant fromCreateDto(CreateTenantDto createTenantDto);

    @Named("applicationToUuids")
    default Set<UUID> mapApplicationToIds(Set<Application> applications) {
        return MapUtils.extractKeysFromSet(applications, Application::getId);
    }

    @Named("paymentToUuids")
    default Set<UUID> mapPaymentToIds(Set<Payment> payments) {
        return MapUtils.extractKeysFromSet(payments, Payment::getId);
    }

    @Named("propertyTenantPaymentApplicationsToIds")
    default Set<Long> mappropertyTenantPaymentApplicationsToIds(
            Set<PropertyTenantPaymentApplication> propertyTenantPaymentApplications) {

        return MapUtils.extractKeysFromSet(propertyTenantPaymentApplications, PropertyTenantPaymentApplication::getId);

    }

}

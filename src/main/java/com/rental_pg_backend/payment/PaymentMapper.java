package com.rental_pg_backend.payment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rental_pg_backend.payment.dto.PaymentDto;
import com.rental_pg_backend.payment.entities.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "tenantId", source = "tenant.id")
    @Mapping(target = "propertyTenantPaymentApplicationId", source = "propertyTenantPaymentApplication.id")
    PaymentDto toDto(Payment payment);

}

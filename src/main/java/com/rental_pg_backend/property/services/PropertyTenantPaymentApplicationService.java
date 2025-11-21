package com.rental_pg_backend.property.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rental_pg_backend.property.dto.PropertyTenantPaymentApplicationDto;
import com.rental_pg_backend.property.entities.PropertyTenantPaymentApplication;
import com.rental_pg_backend.property.mappers.PropertyTenantPaymentApplicationMapper;
import com.rental_pg_backend.property.repositories.PropertyTenantPaymentApplicationRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class PropertyTenantPaymentApplicationService {

    private final PropertyTenantPaymentApplicationRepository propertyTenantPaymentApplicationRepository;
    private final PropertyTenantPaymentApplicationMapper propertyTenantPaymentApplicationMapper;

    public Flux<PropertyTenantPaymentApplicationDto> findPropertyTenantPaymentApplicationWithIds(List<Long> ids) {

        List<PropertyTenantPaymentApplication> propertyTenantPaymentApplications = propertyTenantPaymentApplicationRepository
                .findAllById(ids);

        return Flux.fromIterable(
                propertyTenantPaymentApplications.stream().map(propertyTenantPaymentApplicationMapper::toDto).toList());

    }

}

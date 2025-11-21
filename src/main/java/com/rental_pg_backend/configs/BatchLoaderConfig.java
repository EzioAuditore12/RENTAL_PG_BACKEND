package com.rental_pg_backend.configs;

import java.util.UUID;

import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.BatchLoaderRegistry;

import com.rental_pg_backend.application.ApplicationService;
import com.rental_pg_backend.application.dto.ApplicationDto;
import com.rental_pg_backend.payment.PaymentService;
import com.rental_pg_backend.payment.dto.PaymentDto;
import com.rental_pg_backend.property.dto.property.PropertyDto;
import com.rental_pg_backend.property.services.PropertyService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BatchLoaderConfig {

        private final ApplicationService applicationService;
        private final PaymentService paymentService;
        private final PropertyService propertyService;

        private final BatchLoaderRegistry batchLoaderRegistry;

        @PostConstruct
        public void registerBatchLoaders() {

                batchLoaderRegistry.forTypePair(UUID.class, ApplicationDto.class)
                                .registerBatchLoader((keys, env) -> applicationService.findApplicationWithIds(keys));

                batchLoaderRegistry.forTypePair(UUID.class, PaymentDto.class)
                                .registerBatchLoader((keys, env) -> paymentService.findPaymentWithIds(keys));

                batchLoaderRegistry.forTypePair(UUID.class, PropertyDto.class)
                                .registerBatchLoader((keys, env) -> propertyService.findPropertyWithIds(keys));

        }

}

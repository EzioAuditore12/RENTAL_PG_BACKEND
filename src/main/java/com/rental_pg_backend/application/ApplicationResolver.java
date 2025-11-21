package com.rental_pg_backend.application;

import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

import com.rental_pg_backend.application.dto.ApplicationDto;
import com.rental_pg_backend.application.dto.LeaseDto;
import com.rental_pg_backend.application.services.LeaseService;
import com.rental_pg_backend.auth.guards.AuthenticatedTenantOrManager;
import com.rental_pg_backend.auth.utils.AuthUtils;
import com.rental_pg_backend.property.dto.property.PropertyDto;
import com.rental_pg_backend.property.services.PropertyService;
import com.rental_pg_backend.tenant.TenantService;
import com.rental_pg_backend.tenant.dto.TenantDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ApplicationResolver {

    private final ApplicationService applicationService;

    private final PropertyService propertyService;

    private final TenantService tenantService;

    private final LeaseService leaseService;

    @AuthenticatedTenantOrManager
    @QueryMapping
    public ApplicationDto getApplication(@Argument UUID id) {

        UUID userId = AuthUtils.getAuthenticatedUserId();

        boolean authenticatedManagerOrTenant = applicationService.isAssociatedWithApplication(userId, id);

        if (!authenticatedManagerOrTenant)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Only applicant or manger associated with property can view");

        return applicationService.getApplicationDetails(id);
    }

    @SchemaMapping(typeName = "Application", field = "property")
    public PropertyDto getProperty(ApplicationDto applicationDto) {

        UUID propertyId = applicationDto.getPropertyId();

        return propertyService.getPropertyDetails(propertyId);

    }

    @SchemaMapping(typeName = "Application", field = "lease")
    public LeaseDto getLease(ApplicationDto applicationDto) {

        UUID leaseId = applicationDto.getLeaseId();

        return leaseService.findById(leaseId);

    }

    @SchemaMapping(typeName = "Application", field = "tenant")
    public TenantDto getTenant(ApplicationDto applicationDto) {

        UUID tenantId = applicationDto.getTenantId();

        return tenantService.findById(tenantId);

    }

}

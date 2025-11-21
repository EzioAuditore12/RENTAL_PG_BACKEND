package com.rental_pg_backend.tenant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

import com.rental_pg_backend.application.ApplicationService;
import com.rental_pg_backend.application.dto.ApplicationDto;
import com.rental_pg_backend.application.dto.CreateApplicationDto;
import com.rental_pg_backend.auth.guards.AuthenticatedTenant;
import com.rental_pg_backend.auth.utils.AuthUtils;
import com.rental_pg_backend.payment.dto.PaymentDto;
import com.rental_pg_backend.tenant.dto.TenantDto;
import com.rental_pg_backend.tenant.dto.TenantPublicDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TenantResolver {

    private final TenantService tenantService;
    private final TenantMapper tenantMapper;

    private final ApplicationService applicationService;

    @QueryMapping
    public TenantPublicDto getTenant(@Argument UUID id) {

        TenantDto tenant = tenantService.findById(id);

        if (Objects.isNull(tenant))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such user found");

        return tenantMapper.toPublicDto(tenant);

    }

    @AuthenticatedTenant
    @QueryMapping
    public TenantPublicDto getAuthenticatedTenant() {

        UUID userId = AuthUtils.getAuthenticatedUserId();

        TenantDto tenant = tenantService.findById(userId);

        return tenantMapper.toPublicDto(tenant);

    }

    @AuthenticatedTenant
    @MutationMapping
    public ApplicationDto createApplication(@Argument("input") CreateApplicationDto createApplicationDto) {

        UUID userId = AuthUtils.getAuthenticatedUserId();

        return applicationService.createApplication(userId, createApplicationDto);

    }

    @AuthenticatedTenant
    @QueryMapping
    public List<ApplicationDto> getCreatedApplications() {

        UUID tenantId = AuthUtils.getAuthenticatedUserId();

        log.info("Fetching applications for tenantId: {}", tenantId);

        return tenantService.createdApplications(tenantId);

    }

    @SchemaMapping(typeName = "Tenant", field = "applications")
    public CompletableFuture<List<ApplicationDto>> getApplications(TenantPublicDto tenantPublicDto,
            DataLoader<UUID, ApplicationDto> dataLoader) {

        Set<UUID> ids = tenantPublicDto.getApplicationIds();

        return dataLoader.loadMany(new ArrayList<>(ids));

    }

    @SchemaMapping(typeName = "Tenant", field = "payments")
    public CompletableFuture<List<PaymentDto>> getPayments(TenantPublicDto tenantPublicDto,
            DataLoader<UUID, PaymentDto> dataLoader) {

        Set<UUID> ids = tenantPublicDto.getPaymentIds();

        return dataLoader.loadMany(new ArrayList<>(ids));

    }
}

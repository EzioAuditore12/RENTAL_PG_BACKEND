package com.rental_pg_backend.tenant;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.rental_pg_backend.application.ApplicationMapper;
import com.rental_pg_backend.application.dto.ApplicationDto;
import com.rental_pg_backend.application.entities.Application;
import com.rental_pg_backend.application.repositories.ApplicationRepository;
import com.rental_pg_backend.common.services.CloudinaryService;
import com.rental_pg_backend.tenant.dto.CreateTenantDto;
import com.rental_pg_backend.tenant.dto.TenantDto;
import com.rental_pg_backend.tenant.entities.Tenant;
import com.rental_pg_backend.tenant.repositories.TenantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantService {

    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;
    private final CloudinaryService cloudinaryService;

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;

    public TenantDto findById(UUID id) {
        return tenantRepository.findById(id).map(tenantMapper::toDto).orElse(null);
    }

    public TenantDto findByEmail(String email) {

        return tenantRepository.findByEmail(email).map(tenantMapper::toDto)
                .orElse(null);

    }

    public TenantDto createAccount(CreateTenantDto createTenantDto) {

        Tenant tenant = tenantMapper.fromCreateDto(createTenantDto);

        if (Objects.nonNull(createTenantDto.getAvatar())) {
            String uploadedAvatarUrl = cloudinaryService.uploadFile(createTenantDto.getAvatar());
            tenant.setAvatar(uploadedAvatarUrl);
        }

        Tenant createdAccount = tenantRepository.save(tenant);

        return tenantMapper.toDto(createdAccount);

    }

    public List<ApplicationDto> createdApplications(UUID tenantId) {

        List<Application> applications = applicationRepository.findByTenantId(tenantId);

        log.info("applications: {}", applications);

        return applications.stream().map(applicationMapper::toDto).toList();
    }

}

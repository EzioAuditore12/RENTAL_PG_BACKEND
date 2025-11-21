package com.rental_pg_backend.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rental_pg_backend.application.dto.ApplicationDto;
import com.rental_pg_backend.application.dto.CreateApplicationDto;
import com.rental_pg_backend.application.dto.RespondToApplicationDto;
import com.rental_pg_backend.application.dto.StartLeaseDto;
import com.rental_pg_backend.application.entities.Application;
import com.rental_pg_backend.application.entities.Lease;
import com.rental_pg_backend.application.enums.ApplicationStatusType;
import com.rental_pg_backend.application.repositories.ApplicationRepository;
import com.rental_pg_backend.application.services.LeaseService;
import com.rental_pg_backend.property.entities.Property;
import com.rental_pg_backend.property.entities.PropertyTenantPaymentApplication;
import com.rental_pg_backend.property.repositories.PropertyRepository;
import com.rental_pg_backend.property.repositories.PropertyTenantPaymentApplicationRepository;
import com.rental_pg_backend.tenant.entities.Tenant;
import com.rental_pg_backend.tenant.repositories.TenantRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ApplicationService {

        private final ApplicationRepository applicationRepository;
        private final ApplicationMapper applicationMapper;

        private final TenantRepository tenantRepository;
        private final PropertyRepository propertyRepository;

        private final LeaseService leaseService;

        private final PropertyTenantPaymentApplicationRepository propertyTenantPaymentApplicationRepository;

        @Transactional
        public ApplicationDto createApplication(UUID tenantId, CreateApplicationDto createApplicationDto) {

                Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such tenant found"));

                boolean existingApplication = applicationRepository.existsByTenantIdAndPropertyId(tenantId,
                                createApplicationDto.getPropertyId());

                if (existingApplication)
                        throw new ResponseStatusException(HttpStatus.CONFLICT,
                                        "Application for this property for this use already exists");

                Property property = propertyRepository
                                .findById(createApplicationDto.getPropertyId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "No Such property found"));

                Application application = Application.builder()
                                .tenant(tenant)
                                .property(property)
                                .startDate(LocalDateTime.now())
                                .build();

                Application savedApplication = applicationRepository.save(application);

                return applicationMapper.toDto(savedApplication);

        }

        @Transactional
        public ApplicationDto respondToApplication(UUID managerId, RespondToApplicationDto respondToApplicationDto) {

                Application application = applicationRepository.findById(respondToApplicationDto.getApplicationId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "No Such application Found"));

                UUID propertyManagerId = application.getProperty().getManager().getId();

                if (!Objects.equals(managerId, propertyManagerId))
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                        "Only manager who created property is authorized");

                if (respondToApplicationDto.getStatus() == ApplicationStatusType.DENIED)
                        return rejectApplication(application);

                Lease lease = leaseService.createLease(application.getId(), startLease(respondToApplicationDto));

                application.setStatus(respondToApplicationDto.getStatus());

                application.setLease(lease);

                createJunctionalRecord(application);

                Application savedApplication = applicationRepository.save(application);

                return applicationMapper.toDto(savedApplication);

        }

        public ApplicationDto getApplicationDetails(UUID applicationId) {

                return applicationRepository.findById(applicationId).map(applicationMapper::toDto).orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Such application exists"));

        }

        public boolean isAssociatedWithApplication(UUID userId, UUID applicationId) {
                Application application = applicationRepository.findById(applicationId)
                                .orElse(null);

                if (Objects.isNull(application))
                        return false;

                UUID tenantId = application.getTenant().getId();
                UUID managerId = application.getProperty().getManager().getId();

                return userId.equals(tenantId) || userId.equals(managerId);
        }

        public Flux<ApplicationDto> findApplicationWithIds(List<UUID> ids) {

                List<Application> applications = applicationRepository.findAllById(ids);

                return Flux.fromIterable(
                                applications.stream().map(applicationMapper::toDto).toList());

        }

        private StartLeaseDto startLease(RespondToApplicationDto respondToApplicationDto) {

                return StartLeaseDto.builder()
                                .startDate(respondToApplicationDto.getStartDate())
                                .endDate(respondToApplicationDto.getEndDate())
                                .deposit(respondToApplicationDto.getDeposit())
                                .rent(respondToApplicationDto.getRent())
                                .build();

        }

        private ApplicationDto rejectApplication(Application application) {
                application.setStatus(ApplicationStatusType.DENIED);

                return applicationMapper.toDto(application);
        }

        private void createJunctionalRecord(Application application) {

                boolean exists = propertyTenantPaymentApplicationRepository.existsByApplication_Id(application.getId());

                if (exists)
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Application Already setted");

                PropertyTenantPaymentApplication pta = PropertyTenantPaymentApplication
                                .builder()
                                .application(application)
                                .property(application.getProperty())
                                .tenant(application.getTenant())
                                .payment(null)
                                .build();

                propertyTenantPaymentApplicationRepository.save(pta);

        }

}

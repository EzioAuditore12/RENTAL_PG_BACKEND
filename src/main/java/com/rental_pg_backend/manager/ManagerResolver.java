package com.rental_pg_backend.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.rental_pg_backend.application.ApplicationService;
import com.rental_pg_backend.application.dto.ApplicationDto;
import com.rental_pg_backend.application.dto.RespondToApplicationDto;
import com.rental_pg_backend.auth.guards.AuthenticatedManager;
import com.rental_pg_backend.auth.utils.AuthUtils;
import com.rental_pg_backend.manager.dto.ManagerDto;
import com.rental_pg_backend.manager.dto.ManagerPublicDto;
import com.rental_pg_backend.property.dto.property.PropertyDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import com.rental_pg_backend.common.dto.PaginationDto;
import com.rental_pg_backend.property.dto.property.PropertyPageDto;
import com.rental_pg_backend.property.services.PropertyService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ManagerResolver {

    private final ManagerService managerService;
    private final ManagerMapper managerMapper;
    private final PropertyService propertyService;

    private final ApplicationService applicationService;

    @QueryMapping
    public ManagerPublicDto getManager(@Argument UUID id) {

        ManagerDto manager = managerService.findById(id);

        return managerMapper.toPublicDto(manager);
    }

    @AuthenticatedManager
    @QueryMapping
    public ManagerPublicDto getAuthenticatedManager() {

        UUID managerId = AuthUtils.getAuthenticatedUserId();

        ManagerDto manager = managerService.findById(managerId);

        return managerMapper.toPublicDto(manager);

    }

    @AuthenticatedManager
    @QueryMapping
    public PropertyPageDto getManagedProperties(
            @Argument Integer page,
            @Argument Integer size) {

        UUID managerId = AuthUtils.getAuthenticatedUserId();

        Page<PropertyDto> propertyPage = propertyService.getManagedProperties(managerId, page != null ? page : 0, size != null ? size : 10);

        PaginationDto paginationDto = PaginationDto.builder()
                .currentPage(propertyPage.getNumber())
                .totalPages(propertyPage.getTotalPages())
                .totalElements((int) propertyPage.getTotalElements())
                .size(propertyPage.getSize())
                .build();

        return PropertyPageDto.builder()
                .content(propertyPage.getContent())
                .pagination(paginationDto)
                .build();
    }

    @AuthenticatedManager
    @MutationMapping
    public ApplicationDto respondToApplication(@Argument("input") RespondToApplicationDto respondToApplicationDto) {

        UUID managerId = AuthUtils.getAuthenticatedUserId();

        return applicationService.respondToApplication(managerId, respondToApplicationDto);

    }

    @SchemaMapping(typeName = "Manager", field = "managedProperties")
    public CompletableFuture<List<PropertyDto>> getProperties(ManagerPublicDto managerPublicDto,
            DataLoader<UUID, PropertyDto> dataLoader) {

        Set<UUID> ids = managerPublicDto.getManagedPropertyIds();

        return dataLoader.loadMany(new ArrayList<>(ids));

    }

    @SchemaMapping(typeName = "Manager", field = "managedPropertiesCount")
    public Integer getManagedPropertiesCount(ManagerPublicDto managerPublicDto) {
        return (int) managerService.getManagedPropertiesCount(managerPublicDto.getId());
    }

}

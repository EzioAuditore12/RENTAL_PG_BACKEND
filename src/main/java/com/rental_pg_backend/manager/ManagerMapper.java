package com.rental_pg_backend.manager;

import java.util.Set;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.rental_pg_backend.common.utils.MapUtils;
import com.rental_pg_backend.manager.dto.CreateManagerDto;
import com.rental_pg_backend.manager.dto.ManagerDto;
import com.rental_pg_backend.manager.dto.ManagerPublicDto;
import com.rental_pg_backend.manager.entities.Manager;
import com.rental_pg_backend.property.entities.Property;

@Mapper(componentModel = "spring")
public interface ManagerMapper {

    @Mapping(source = "managedProperties", target = "managedPropertyIds", qualifiedByName = "managedPropertyToUuids")
    ManagerDto toDto(Manager manager);

    ManagerPublicDto toPublicDto(ManagerDto managerDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "managedProperties", ignore = true)
    Manager fromCreateDto(CreateManagerDto createManagerDto);

    @Named("managedPropertyToUuids")
    default Set<UUID> managedPropertiesToUuids(Set<Property> properties) {
        return MapUtils.extractKeysFromSet(properties, Property::getId);
    }

}

package com.rental_pg_backend.application.mapppers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rental_pg_backend.application.dto.LeaseDto;
import com.rental_pg_backend.application.entities.Lease;

@Mapper(componentModel = "spring")
public interface LeaseMapper {

    @Mapping(target = "applicationId", source = "application.id")
    LeaseDto toDto(Lease lease);

}

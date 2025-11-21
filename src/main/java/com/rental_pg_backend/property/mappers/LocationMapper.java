package com.rental_pg_backend.property.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rental_pg_backend.property.dto.location.LocationDto;
import com.rental_pg_backend.property.entities.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "longitude", source = "coordinates.x")
    @Mapping(target = "latitude", source = "coordinates.y")
    LocationDto toDto(Location location);

}

package com.rental_pg_backend.property.services;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rental_pg_backend.common.services.CloudinaryService;
import com.rental_pg_backend.manager.ManagerService;
import com.rental_pg_backend.property.dto.location.InsertLocationDto;
import com.rental_pg_backend.property.dto.location.LocationDto;
import com.rental_pg_backend.property.dto.nominatim.NominatimApiResponseDto;
import com.rental_pg_backend.property.dto.property.CreatePropertyDto;
import com.rental_pg_backend.property.dto.property.PropertyDto;
import com.rental_pg_backend.property.dto.property.PropertySearchDto;
import com.rental_pg_backend.property.entities.Location;
import com.rental_pg_backend.property.entities.Property;
import com.rental_pg_backend.property.mappers.PropertyMapper;
import com.rental_pg_backend.property.repositories.PropertyRepository;
import com.rental_pg_backend.property.specifications.PropertySpecification;
import com.rental_pg_backend.property.utils.NominatimUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;

    private final ManagerService managerService;

    private final LocationService locationService;

    private final CloudinaryService cloudinaryService;

    @Transactional
    public PropertyDto initializeProperty(UUID managerId, CreatePropertyDto createPropertyDto) {

        NominatimApiResponseDto nominatimApiResponseDto = buildLocationWithNominatim(
                createPropertyDto.getLatitude(),
                createPropertyDto.getLongitude());

        if (Objects.isNull(nominatimApiResponseDto))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find such location");

        LocationDto locationDto = locationService.findLocationByLongitudeAndLatitude(
                Double.parseDouble(nominatimApiResponseDto.getLon()),
                Double.parseDouble(nominatimApiResponseDto.getLat()));

        if (Objects.nonNull(locationDto))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A property with this location is already registered");

        List<String> uploadedImageUrls = cloudinaryService.uploadMultipleFiles(createPropertyDto.getPhotos());

        Property property = propertyMapper.toCreateEntity(createPropertyDto);

        property.setPhotoUrls(uploadedImageUrls);

        managerService.saveProperty(managerId, property);

        Property savedProperty = propertyRepository.save(property);

        InsertLocationDto insertLocationDto = buildLocation(createPropertyDto, nominatimApiResponseDto);

        Location savedLocation = locationService.insertSavedLocation(insertLocationDto, savedProperty);

        savedProperty.setLocation(savedLocation);

        return propertyMapper.toDto(savedProperty);
    }

    public PropertyDto getPropertyDetails(UUID propertyId) {

        return propertyRepository.findById(propertyId).map(propertyMapper::toDto).orElse(null);

    }

    public Page<PropertyDto> getAllPropertiesBySearch(PropertySearchDto propertySearchDto, Integer page, Integer size) {

        Specification<Property> spec = PropertySpecification.withDynamicQuery(propertySearchDto);

        Pageable pageable = PageRequest.of(page, size);

        return propertyRepository.findAll(spec, pageable).map(propertyMapper::toDto);

    }

    public Flux<PropertyDto> findPropertyWithIds(List<UUID> ids) {

        List<Property> properties = propertyRepository.findAllById(ids);

        return Flux.fromIterable(
                properties.stream().map(propertyMapper::toDto).toList());

    }

    private InsertLocationDto buildLocation(CreatePropertyDto createPropertyDto,
            NominatimApiResponseDto nominatimApiResponseDto) {

        return InsertLocationDto.builder()
                .address(createPropertyDto.getAddress())
                .city(createPropertyDto.getCity())
                .state(createPropertyDto.getState())
                .country(createPropertyDto.getCountry())
                .postalCode(createPropertyDto.getPostalCode())
                .longitude(Double.parseDouble(nominatimApiResponseDto.getLon()))
                .latitude(Double.parseDouble(nominatimApiResponseDto.getLat()))
                .build();
    }

    private NominatimApiResponseDto buildLocationWithNominatim(Double latitude, Double longitude) {

        NominatimApiResponseDto nomantimApiResponseDto = NominatimUtils.getReverseGeoLocationDetails(latitude,
                longitude);

        if (Objects.isNull(nomantimApiResponseDto))
            return null;

        return nomantimApiResponseDto;
    }

}

package com.rental_pg_backend.seeders;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental_pg_backend.property.dto.location.InsertLocationDto;
import com.rental_pg_backend.property.dto.location.LocationDto;
import com.rental_pg_backend.property.dto.nominatim.NominatimApiResponseDto;
import com.rental_pg_backend.property.entities.Location;
import com.rental_pg_backend.property.repositories.LocationRepository;
import com.rental_pg_backend.property.services.LocationService;
import com.rental_pg_backend.property.services.PropertyService;
import com.rental_pg_backend.seeders.dto.SeederPlaceDto;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.rental_pg_backend.manager.entities.Manager;
import com.rental_pg_backend.manager.repositories.ManagerRepository;
import com.rental_pg_backend.property.entities.Property;
import com.rental_pg_backend.property.enums.AmenityType;
import com.rental_pg_backend.property.enums.HighlightType;
import com.rental_pg_backend.property.enums.PropertyType;
import com.rental_pg_backend.property.repositories.PropertyRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class PropertyManagerSeeder implements CommandLineRunner {

    private final ManagerRepository managerRepository;

    private final PropertyService propertyService;
    private final PropertyRepository propertyRepository;

    private final LocationService locationService;
    private final LocationRepository locationRepository;

    @Value("${app.seed.enabled.manager.property}")
    private boolean seedEnabled;


    @Override
    @Transactional
    public void run(String... args) {
        if (!seedEnabled) {
            return;
        }
        List<Manager> managers = managerRepository.findAll();
        if (managers.isEmpty()) return;
        seedProperties(managers);
        System.out.println("100 properties seeded");
    }

    @Transactional
    protected void seedProperties(List<Manager> managers) {
        Random random = new Random();
        List<SeederPlaceDto> places = loadPlacesFromJson();
        for (int i = 1; i <= 100; i++) {
            seedSingleProperty(i, managers, random, places);
            try {
                Thread.sleep(1200); // 1.2 seconds delay to avoid API rate limit
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Transactional
    protected void seedSingleProperty(int i,
                                      List<Manager> managers,
                                      Random random,
                                      List<SeederPlaceDto> places) {
        SeederPlaceDto place = places.get((i - 1) % places.size());

        double latitude = place.getLatitude();
        double longitude = place.getLongitude();

        NominatimApiResponseDto nominatimApiResponseDto = propertyService.buildLocationWithNominatim(latitude, longitude);
        if (Objects.isNull(nominatimApiResponseDto)) return;

        LocationDto locationDto = locationService.findLocationByLongitudeAndLatitude(
                Double.parseDouble(nominatimApiResponseDto.getLon()),
                Double.parseDouble(nominatimApiResponseDto.getLat()));
        if (Objects.nonNull(locationDto)) return;

        // 1. Pick 2 to 4 random images
        int numImages = 2 + random.nextInt(3);
        List<String> uploadedImageUrls = getRandomImages(numImages, random);

        // 2. Pick random amenities (2 to 5 amenities)
        List<AmenityType> randomAmenities = getRandomEnums(AmenityType.class, 2 + random.nextInt(4), random);

        // 3. Pick random highlights (1 to 3 highlights)
        List<HighlightType> randomHighlights = getRandomEnums(HighlightType.class, 1 + random.nextInt(3), random);

        // 4. Pick a random property type
        PropertyType randomPropertyType = PropertyType.values()[random.nextInt(PropertyType.values().length)];


        String propertyName = "Luxury PG " + i;
        Property property = Property.builder()
                .name(propertyName)
                .description("Fully furnished PG in Dehradun with modern amenities.")
                .pricePerMonth(5000.0 + random.nextInt(15000))
                .securityDeposit(2000.0 + random.nextInt(5000))
                .beds(1 + random.nextInt(4))
                .baths(1 + random.nextInt(3))
                .squareFeet(200.0 + random.nextInt(1000))
                .petAllowed(random.nextBoolean())
                .parkingIncluded(random.nextBoolean())
                .propertyType(randomPropertyType) // Applied random type
                .amenities(randomAmenities)       // Applied random amenities
                .highlights(randomHighlights)     // Applied random highlights
                .photoUrls(uploadedImageUrls)     // Applied random images
                .build();
        property.setManager(managers.get(random.nextInt(managers.size())));

        Property savedProperty = propertyRepository.save(property);

        InsertLocationDto insertLocationDto = InsertLocationDto.builder()
                .address(place.getPlace())
                .city(place.getCity())
                .state(place.getState())
                .postalCode(place.getPostalCode())
                .longitude(Double.parseDouble(nominatimApiResponseDto.getLon()))
                .latitude(Double.parseDouble(nominatimApiResponseDto.getLat()))
                .build();

        Location savedLocation = locationService.insertSavedLocation(insertLocationDto, savedProperty);
        savedProperty.setLocation(savedLocation);
        propertyRepository.save(savedProperty);
    }

    private List<SeederPlaceDto> loadPlacesFromJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(
                    getClass().getResourceAsStream("/data/uttarakhand_places.json"),
                    new TypeReference<List<SeederPlaceDto>>() {
                    }
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to load places JSON", e);
        }
    }

    private List<String> getRandomImages(int count, Random random) {
        List<String> copy = new ArrayList<>(IMAGE_POOL);
        Collections.shuffle(copy, random);
        return copy.subList(0, Math.min(count, copy.size()));
    }

    private <E extends Enum<E>> List<E> getRandomEnums(Class<E> enumClass, int count, Random random) {
        List<E> enumValues = new ArrayList<>(Arrays.asList(enumClass.getEnumConstants()));
        Collections.shuffle(enumValues, random);
        return enumValues.subList(0, Math.min(count, enumValues.size()));
    }

    // A pool of high-quality property images to choose from randomly
    private static final List<String> IMAGE_POOL = List.of(
            "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85",
            "https://images.unsplash.com/photo-1494526585095-c41746248156",
            "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2",
            "https://images.unsplash.com/photo-1518780664697-55e3ad937233",
            "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267",
            "https://images.unsplash.com/photo-1502672260266-1c1f08b3e8e7",
            "https://images.unsplash.com/photo-1484154218962-a197022b5858",
            "https://images.unsplash.com/photo-1512918728675-ed5a9ecdebfd",
            "https://images.unsplash.com/photo-1497366216548-37526070297c",
            "https://images.unsplash.com/photo-1564013799919-ab600027ffc6",
            "https://images.unsplash.com/photo-1583847268964-b28dc8f51f92",
            "https://images.unsplash.com/photo-1574362848149-11496d93a7c7",
            "https://images.unsplash.com/photo-1598928506311-c55d43f07a01",
            "https://images.unsplash.com/photo-1554995207-c18c203602cb",
            "https://images.unsplash.com/photo-1505691938895-1758d7feb511",
            "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9",
            "https://images.unsplash.com/photo-1600607687931-ceeb66d11316",
            "https://images.unsplash.com/photo-1600585154340-be6161a56a0c",
            "https://images.unsplash.com/photo-1513694203232-719a280e022f",
            "https://images.unsplash.com/photo-1536376072261-38c75010e6c9"
    );
}

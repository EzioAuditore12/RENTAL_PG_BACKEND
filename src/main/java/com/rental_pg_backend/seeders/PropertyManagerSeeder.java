package com.rental_pg_backend.seeders;

import java.util.*;

import com.rental_pg_backend.property.dto.location.InsertLocationDto;
import com.rental_pg_backend.property.dto.location.LocationDto;
import com.rental_pg_backend.property.dto.nominatim.NominatimApiResponseDto;
import com.rental_pg_backend.property.entities.Location;
import com.rental_pg_backend.property.repositories.LocationRepository;
import com.rental_pg_backend.property.services.LocationService;
import com.rental_pg_backend.property.services.PropertyService;
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
        List<String> places = getPlaces();
        List<Double[]> coordinates = getCoordinates();
        for (int i = 1; i <= 100; i++) {
            seedSingleProperty(i, managers, random, places, coordinates);
            try {
                Thread.sleep(1200); // 1.2 seconds delay to avoid API rate limit
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Transactional
    protected void seedSingleProperty(int i, List<Manager> managers, Random random, List<String> places, List<Double[]> coordinates) {
        Double[] baseCord = coordinates.get((i - 1) % coordinates.size());
        double latitude = baseCord[0];
        double longitude = baseCord[1];

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
                .address(places.get((i - 1) % places.size()))
                .city("Dehradun")
                .state("Uttarakhand")
                .country("India")
                .postalCode("248001")
                .longitude(Double.parseDouble(nominatimApiResponseDto.getLon()))
                .latitude(Double.parseDouble(nominatimApiResponseDto.getLat()))
                .build();

        Location savedLocation = locationService.insertSavedLocation(insertLocationDto, savedProperty);
        savedProperty.setLocation(savedLocation);
        propertyRepository.save(savedProperty);
    }

    private List<String> getPlaces() {
        return List.of(
                "Rajpur Road", "Clock Tower", "Prem Nagar", "ISBT", "Jakhan",
                "Ballupur", "Patel Nagar", "Raipur", "Clement Town", "GMS Road",
                "Vasant Vihar", "Dalanwala", "Indira Nagar", "Kaulagarh", "Dharampur",
                "Karanpur", "Nehru Colony", "Race Course", "Majra", "Subhash Nagar",
                "Sahastradhara Road", "Garhi Cantt", "Hathibarkala", "Canal Road", "Aamwala",
                "Chukkuwala", "Kishanpur", "Turner Road", "Transport Nagar", "Sewla Kalan",
                "Banjarawala", "Mothrowala", "Ajabpur Kalan", "Pondha", "Selaqui",
                "Sudhowala", "Bidholi", "Jhajra", "Makkawala", "Gujrara",
                "Tunwala", "Balawala", "Miyawala", "Harrawala", "Kuanwala",
                "Nawada", "Nathanpur", "Jogiwala", "Brahmanwala", "Niranjanpur",
                "Kargi", "Bhandari Bagh", "Rest Camp", "Araghar", "D.L. Road",
                "Tyagi Road", "Gandhi Road", "Haridwar Road", "Saharanpur Road", "EC Road",
                "Chakrata Road", "Shimla Bypass", "Neshvilla Road", "Kanwali Road", "Railway Station",
                "Panditwari", "Vihar Colony", "Vijay Colony", "Dhoran Khas", "Govindgarh",
                "Khurbura", "Lakkhi Bagh", "Machhi Bazar", "Malsi", "Manduwala",
                "Mehuwala", "Mohkampur", "Nalapani", "Nanda Ki Chowki", "Naugaon",
                "Niranjanpur Mandi", "Pacific Mall Area", "Paltan Bazaar", "Phulsani", "Purkul",
                "Salawala", "Sayedwala", "Industrial Area", "Shastri Nagar", "Sheeshambada",
                "Suman Nagar", "Tarla Adhoiwala", "THDC Colony", "Vani Vihar", "Vasant Vihar Phase 1",
                "Vasant Vihar Phase 2", "Vidyut Vihar", "Wadia Institute Area", "Yamuna Colony", "Johri Village"
        );
    }

    private List<Double[]> getCoordinates() {
        return List.of(
                new Double[]{30.3255, 78.0436}, new Double[]{30.3243, 78.0418}, new Double[]{30.3348, 77.9501},
                new Double[]{30.2881, 78.0402}, new Double[]{30.3572, 78.0704}, new Double[]{30.3300, 78.0160},
                new Double[]{30.3060, 78.0150}, new Double[]{30.3161, 78.0899}, new Double[]{30.2670, 78.0050},
                new Double[]{30.3180, 78.0110}, new Double[]{30.3280, 77.9950}, new Double[]{30.3200, 78.0550},
                new Double[]{30.3350, 78.0000}, new Double[]{30.3400, 78.0100}, new Double[]{30.3050, 78.0450},
                new Double[]{30.3250, 78.0550}, new Double[]{30.2950, 78.0500}, new Double[]{30.3100, 78.0400},
                new Double[]{30.2850, 78.0200}, new Double[]{30.2750, 78.0150}, new Double[]{30.3500, 78.0800},
                new Double[]{30.3450, 78.0300}, new Double[]{30.3400, 78.0450}, new Double[]{30.3650, 78.0750},
                new Double[]{30.3400, 78.0700}, new Double[]{30.3250, 78.0350}, new Double[]{30.3600, 78.0750},
                new Double[]{30.2750, 78.0250}, new Double[]{30.2800, 78.0100}, new Double[]{30.2900, 78.0150},
                new Double[]{30.2700, 78.0400}, new Double[]{30.2650, 78.0500}, new Double[]{30.2950, 78.0400},
                new Double[]{30.3600, 77.9300}, new Double[]{30.3650, 77.8500}, new Double[]{30.3450, 77.9200},
                new Double[]{30.4050, 77.9650}, new Double[]{30.3350, 77.9000}, new Double[]{30.3800, 78.0650},
                new Double[]{30.3450, 78.0900}, new Double[]{30.2900, 78.1100}, new Double[]{30.2750, 78.1150},
                new Double[]{30.2800, 78.0950}, new Double[]{30.2650, 78.1200}, new Double[]{30.2550, 78.1300},
                new Double[]{30.2950, 78.0750}, new Double[]{30.3000, 78.0850}, new Double[]{30.2900, 78.0650},
                new Double[]{30.2950, 78.0250}, new Double[]{30.3050, 78.0200}, new Double[]{30.2850, 78.0350},
                new Double[]{30.3150, 78.0350}, new Double[]{30.3100, 78.0500}, new Double[]{30.3050, 78.0550},
                new Double[]{30.3300, 78.0500}, new Double[]{30.3150, 78.0450}, new Double[]{30.3200, 78.0400},
                new Double[]{30.3050, 78.0600}, new Double[]{30.3000, 78.0300}, new Double[]{30.3250, 78.0500},
                new Double[]{30.3300, 78.0250}, new Double[]{30.2950, 77.9800}, new Double[]{30.3350, 78.0400},
                new Double[]{30.3100, 78.0150}, new Double[]{30.3150, 78.0300}, new Double[]{30.3350, 77.9800},
                new Double[]{30.3400, 78.0200}, new Double[]{30.3550, 78.0450}, new Double[]{30.3650, 78.0850},
                new Double[]{30.3250, 78.0200}, new Double[]{30.3200, 78.0350}, new Double[]{30.3150, 78.0400},
                new Double[]{30.3200, 78.0450}, new Double[]{30.3850, 78.0750}, new Double[]{30.3700, 77.9100},
                new Double[]{30.3050, 78.0000}, new Double[]{30.2900, 78.0600}, new Double[]{30.3300, 78.0900},
                new Double[]{30.3450, 77.9600}, new Double[]{30.3900, 77.9400}, new Double[]{30.3000, 78.0250},
                new Double[]{30.3600, 78.0700}, new Double[]{30.3250, 78.0400}, new Double[]{30.3800, 77.9800},
                new Double[]{30.3950, 78.0700}, new Double[]{30.3350, 78.0550}, new Double[]{30.3250, 77.9900},
                new Double[]{30.3600, 77.8600}, new Double[]{30.3100, 78.0700}, new Double[]{30.3700, 77.8300},
                new Double[]{30.3000, 78.0500}, new Double[]{30.3350, 78.0650}, new Double[]{30.2800, 78.0300},
                new Double[]{30.3050, 78.0800}, new Double[]{30.3250, 77.9950}, new Double[]{30.3200, 77.9900},
                new Double[]{30.3300, 78.0000}, new Double[]{30.3150, 78.0100}, new Double[]{30.3300, 78.0350},
                new Double[]{30.3750, 78.0850}
        );
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

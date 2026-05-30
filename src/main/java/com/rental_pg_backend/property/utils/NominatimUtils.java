package com.rental_pg_backend.property.utils;

import java.util.Objects;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.rental_pg_backend.property.dto.nominatim.NominatimApiResponseDto;
import com.rental_pg_backend.property.dto.nominatim.NominatimSearchLocationDto;

public class NominatimUtils {

    private NominatimUtils() {
    }

    private static final String USER_AGENT = "RealEstateApp (realEstate@gmail.com)";

    public static NominatimApiResponseDto getGeoLocationDetails(NominatimSearchLocationDto nominatimSearchLocationDto) {
        // Try structured search first
        String url = nomantimGeoCodingUrl(nominatimSearchLocationDto);
        NominatimApiResponseDto result = executeSearch(url);

        if (result != null) {
            return result;
        }

        // Fallback: free-text search (works better for many Indian cities like Noida)
        String fallbackUrl = nominatimFreeTextSearchUrl(nominatimSearchLocationDto);
        return executeSearch(fallbackUrl);
    }

    private static NominatimApiResponseDto executeSearch(String url) {
        HttpEntity<String> entity = buildNominatimHeaders();
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<NominatimApiResponseDto[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                NominatimApiResponseDto[].class);

        NominatimApiResponseDto[] results = response.getBody();
        if (results != null && results.length > 0) {
            return results[0];
        }
        return null;
    }

    public static NominatimApiResponseDto getReverseGeoLocationDetails(Double latitude, Double longitude) {
        String url = nomantimReverseGeocodingUrl(latitude, longitude);
        HttpEntity<String> entity = buildNominatimHeaders();
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<NominatimApiResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                NominatimApiResponseDto.class);

        if (Objects.isNull(response.getBody()))
            return null;

        return response.getBody();
    }

    private static String nomantimGeoCodingUrl(NominatimSearchLocationDto nominatimSearchLocationDto) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("nominatim.openstreetmap.org")
                .path("/search");

        if (nominatimSearchLocationDto.getStreet() != null && !nominatimSearchLocationDto.getStreet().isEmpty()) {
            builder.queryParam("street", nominatimSearchLocationDto.getStreet());
        }
        if (nominatimSearchLocationDto.getCity() != null && !nominatimSearchLocationDto.getCity().isEmpty()) {
            builder.queryParam("city", nominatimSearchLocationDto.getCity());
        }
        if (nominatimSearchLocationDto.getState() != null && !nominatimSearchLocationDto.getState().isEmpty()) {
            builder.queryParam("state", nominatimSearchLocationDto.getState());
        }
        if (nominatimSearchLocationDto.getCountry() != null && !nominatimSearchLocationDto.getCountry().isEmpty()) {
            builder.queryParam("country", nominatimSearchLocationDto.getCountry());
        }
        if (nominatimSearchLocationDto.getPostalCode() != null && !nominatimSearchLocationDto.getPostalCode().isEmpty()) {
            builder.queryParam("postalcode", nominatimSearchLocationDto.getPostalCode());
        }

        return builder.queryParam("format", "json")
                .queryParam("limit", 1)
                .toUriString();
    }

    private static String nominatimFreeTextSearchUrl(NominatimSearchLocationDto dto) {
        StringBuilder queryBuilder = new StringBuilder();
        if (dto.getCity() != null && !dto.getCity().isEmpty()) {
            queryBuilder.append(dto.getCity());
        }
        if (dto.getState() != null && !dto.getState().isEmpty()) {
            if (queryBuilder.length() > 0) queryBuilder.append(", ");
            queryBuilder.append(dto.getState());
        }
        if (dto.getCountry() != null && !dto.getCountry().isEmpty()) {
            if (queryBuilder.length() > 0) queryBuilder.append(", ");
            queryBuilder.append(dto.getCountry());
        }

        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("nominatim.openstreetmap.org")
                .path("/search")
                .queryParam("q", queryBuilder.toString())
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .toUriString();
    }

    private static String nomantimReverseGeocodingUrl(Double latitude, Double longitude) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("nominatim.openstreetmap.org")
                .path("/reverse")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("format", "json")
                .toUriString();
    }

    private static HttpEntity<String> buildNominatimHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", USER_AGENT);
        return new HttpEntity<>(headers);
    }

}

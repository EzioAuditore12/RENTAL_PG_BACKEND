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
        String url = nomantimGeoCodingUrl(nominatimSearchLocationDto);
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
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("nominatim.openstreetmap.org")
                .path("/search")
                .queryParam("country", nominatimSearchLocationDto.getCountry())
                .queryParam("postalcode", nominatimSearchLocationDto.getPostalCode())
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

package com.rental_pg_backend.seeders.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeederPlaceDto {
    private String place;
    private String city;
    private String state;
    private String postalCode;
    private Double latitude;
    private Double longitude;

}
package com.rental_pg_backend.seeders.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SeederPlaceDto {
    private String place;
    private String city;
    private String state;
    private String postalCode;
    private Double latitude;
    private Double longitude;

}
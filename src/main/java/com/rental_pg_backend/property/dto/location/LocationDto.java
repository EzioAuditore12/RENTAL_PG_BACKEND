package com.rental_pg_backend.property.dto.location;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    private UUID id;

    private String address;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private Double longitude;

    private Double latitude;

}

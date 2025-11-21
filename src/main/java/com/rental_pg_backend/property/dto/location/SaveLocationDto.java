package com.rental_pg_backend.property.dto.location;

import org.locationtech.jts.geom.Point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveLocationDto {

    private String address;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private Point coordinates;
}

package com.rental_pg_backend.property.dto.nominatim;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NominatimAddress {

    private String city;

    private String state;

    private String country;

    private String country_code;

    private String postcode;
}

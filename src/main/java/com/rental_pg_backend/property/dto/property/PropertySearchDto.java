package com.rental_pg_backend.property.dto.property;

import java.util.List;

import com.rental_pg_backend.common.dto.NumericRangesDto;
import com.rental_pg_backend.property.enums.AmenityType;
import com.rental_pg_backend.property.enums.HighlightType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertySearchDto {

    private NumericRangesDto pricePerMonth;

    private NumericRangesDto securityDeposit;

    private List<AmenityType> amenities;

    private List<HighlightType> highlights;

    private boolean petAllowed;

    private boolean parkingIncluded;

    private NumericRangesDto beds;

    private NumericRangesDto baths;

    private NumericRangesDto squareFeet;

    private NumericRangesDto averageRatings;

    private NumericRangesDto numberOfRatings;

    private String address;

    private String city;

    private String state;

    private String country;

    private String postalCode;

}

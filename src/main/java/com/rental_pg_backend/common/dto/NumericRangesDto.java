package com.rental_pg_backend.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NumericRangesDto {

    private Double lt;

    private Double lte;

    private Double gt;

    private Double gte;

    private Double eq;

}

package com.rental_pg_backend.application.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StartLeaseDto {

    @Builder.Default
    private LocalDateTime startDate = LocalDateTime.now();

    private LocalDateTime endDate;

    @Builder.Default
    private Double rent = 0.0;

    @Builder.Default
    private Double deposit = 0.0;
}

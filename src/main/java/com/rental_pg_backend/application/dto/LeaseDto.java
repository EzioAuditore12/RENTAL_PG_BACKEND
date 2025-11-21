package com.rental_pg_backend.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaseDto {

    private UUID id;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Double rent;

    private Double deposit;

    private UUID applicationId;
}

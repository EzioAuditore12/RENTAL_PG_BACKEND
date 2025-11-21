package com.rental_pg_backend.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.rental_pg_backend.application.enums.ApplicationStatusType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespondToApplicationDto {

    private UUID applicationId;

    private ApplicationStatusType status;

    private LocalDateTime startDate = LocalDateTime.now();

    private LocalDateTime endDate;

    private Double rent = 0.0;

    private Double deposit = 0.0;

}

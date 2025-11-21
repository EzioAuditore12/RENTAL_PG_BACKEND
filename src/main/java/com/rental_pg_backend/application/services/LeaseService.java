package com.rental_pg_backend.application.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rental_pg_backend.application.dto.LeaseDto;
import com.rental_pg_backend.application.dto.StartLeaseDto;
import com.rental_pg_backend.application.entities.Lease;
import com.rental_pg_backend.application.mapppers.LeaseMapper;
import com.rental_pg_backend.application.repositories.LeaseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaseService {

    private final LeaseRepository leaseRepository;
    private final LeaseMapper leaseMapper;

    public Lease createLease(UUID applicationId, StartLeaseDto startLeaseDto) {

        boolean existingLease = leaseRepository.existsByApplication_Id(applicationId);

        if (existingLease)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Lease already exists for this application");

        return Lease.builder()
                .startDate(startLeaseDto.getStartDate())
                .endDate(startLeaseDto.getEndDate())
                .rent(startLeaseDto.getRent())
                .deposit(startLeaseDto.getDeposit())
                .build();

    }

    public LeaseDto findById(UUID leaseId) {
        return leaseRepository.findById(leaseId).map(leaseMapper::toDto).orElse(null);
    }

}

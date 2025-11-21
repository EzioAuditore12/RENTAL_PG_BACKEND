package com.rental_pg_backend.payment.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rental_pg_backend.payment.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

}

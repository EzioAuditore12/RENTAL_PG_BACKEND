package com.rental_pg_backend.payment.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.rental_pg_backend.payment.enums.PaymentStatusType;
import com.rental_pg_backend.property.entities.PropertyTenantPaymentApplication;
import com.rental_pg_backend.tenant.entities.Tenant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private double amountDue = 0;

    @Column(nullable = false)
    private Double amountPaid = 0.0;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Column(nullable = false)
    private PaymentStatusType status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PropertyTenantPaymentApplication propertyTenantPaymentApplication;

}

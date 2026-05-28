package com.rental_pg_backend.payment.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.rental_pg_backend.payment.enums.PaymentStatusType;
import com.rental_pg_backend.property.entities.PropertyTenantPaymentApplication;
import com.rental_pg_backend.tenant.entities.Tenant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_payment_tenant", columnList = "tenant_id"),
                @Index(name = "idx_payment_status", columnList = "status"),
                @Index(name = "idx_payment_due_date", columnList = "dueDate"),
                @Index(name = "idx_payment_payment_date", columnList = "paymentDate")
        }
)
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

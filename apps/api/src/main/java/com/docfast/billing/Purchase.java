package com.docfast.billing;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Plan plan;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "mp_preference_id", length = 100)
    private String mpPreferenceId;

    @Column(name = "mp_payment_id", length = 100)
    private String mpPaymentId;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PurchaseStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "paid_at")
    private Instant paidAt;

    protected Purchase() {
    }

    public Purchase(String email, Plan plan, BigDecimal amount, String mpPreferenceId) {
        this.email = email;
        this.plan = plan;
        this.amount = amount;
        this.mpPreferenceId = mpPreferenceId;
        this.status = PurchaseStatus.PENDING;
        this.createdAt = Instant.now();
    }

    public void markAsPaid(String mpPaymentId) {
        this.status = PurchaseStatus.APPROVED;
        this.mpPaymentId = mpPaymentId;
        this.paidAt = Instant.now();
    }

    public void markAsFailed() {
        this.status = PurchaseStatus.FAILED;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Plan getPlan() {
        return plan;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getMpPreferenceId() {
        return mpPreferenceId;
    }

    public String getMpPaymentId() {
        return mpPaymentId;
    }

    public PurchaseStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public enum Plan {
        LIGHT, PRO, POWER
    }

    public enum PurchaseStatus {
        PENDING, APPROVED, FAILED, REFUNDED
    }
}

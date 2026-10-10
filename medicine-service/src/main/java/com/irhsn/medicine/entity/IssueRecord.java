package com.irhsn.medicine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;   // date AND time, like 2026-10-10 17:30

// One row = "this many units were given out from this batch, to this patient".
@Entity
@Table(name = "issue_record")
public class IssueRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "Which batch did the medicine come out of?"
    // Many issue records can come from the same batch,
    // so this is @ManyToOne again, stored as column "stock_id".
    // Through the batch I can also find the medicine:
    // issue.getStock().getMedicine().getName()
    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    // How many units were given out.
    // @Positive = must be more than 0 (you can't give out 0 or -5 tablets).
    @NotNull
    @Positive
    private Integer quantity;

    // "Which patient got it?"
    // Just a number, because residents live in Tvisha's service.
    private Long residentId;

    // "Which appointment was it for?"
    // Just a number, because appointments live in Bhavya's service.
    // Can be empty (null) for walk-ins or health camps.
    private Long appointmentId;

    // Name of the pharmacist or health worker who gave it out.
    @Column(length = 60)
    private String issuedBy;

    // When it was given out.
    private LocalDateTime issuedAt;

    // ===== AUTOMATIC DATE =====
    // @PrePersist = "run this method just before the row is saved for the first time".
    // So if nobody set issuedAt, it gets filled with the current date and time.
    @PrePersist
    public void onCreate() {
        if (issuedAt == null) {
            issuedAt = LocalDateTime.now();
        }
    }

    public IssueRecord() {
    }

    // ===== GETTERS AND SETTERS =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getResidentId() {
        return residentId;
    }

    public void setResidentId(Long residentId) {
        this.residentId = residentId;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }
}
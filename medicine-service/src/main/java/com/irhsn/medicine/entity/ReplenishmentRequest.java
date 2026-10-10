package com.irhsn.medicine.entity;

import com.irhsn.medicine.enums.Priority;
import com.irhsn.medicine.enums.RequestSource;
import com.irhsn.medicine.enums.RequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

// One row = one "please restock this medicine at this centre" request.
@Entity
@Table(name = "replenishment_request")
public class ReplenishmentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "Which medicine do we need more of?"
    // Many requests (over time) can be for the same medicine.
    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    // "Which centre needs it?" Just a number (Bhavya's service).
    @NotNull
    private Long centreId;

    // The centre's village name, copied in when the request is made.
    // Why copy it? So the dashboard can show the village
    // without calling Bhavya's service for every single row.
    @Column(length = 50)
    private String village;

    // How many units the centre had when the request was made.
    private Integer currentQty;

    // How many units we are asking for. Must be more than 0.
    @NotNull
    @Positive
    private Integer requestedQty;

    // Where the request is in its journey:
    // PENDING -> APPROVED -> DISPATCHED -> RECEIVED  (or REJECTED)
    // "= RequestStatus.PENDING" means every new request starts as PENDING
    // unless someone sets it to something else.
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    // How urgent it is: HIGH, MEDIUM or LOW.
    // The service decides this (e.g. stock is 0 -> HIGH).
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Priority priority;

    // Who made the request:
    // AUTO = the system did it, MANUAL = a health worker filled the form.
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private RequestSource source;

    // A note from the officer, like "Dispatch from district store".
    @Column(length = 255)
    private String remarks;

    // When the request was made, and when it last changed.
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ===== AUTOMATIC DATES =====

    // Runs just before the request is saved for the FIRST time:
    // set both dates to "now".
    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    // Runs just before an EXISTING request is updated
    // (e.g. officer approves it): refresh updatedAt.
    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public ReplenishmentRequest() {
    }

    // ===== GETTERS AND SETTERS =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public Long getCentreId() {
        return centreId;
    }

    public void setCentreId(Long centreId) {
        this.centreId = centreId;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public Integer getCurrentQty() {
        return currentQty;
    }

    public void setCurrentQty(Integer currentQty) {
        this.currentQty = currentQty;
    }

    public Integer getRequestedQty() {
        return requestedQty;
    }

    public void setRequestedQty(Integer requestedQty) {
        this.requestedQty = requestedQty;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public RequestSource getSource() {
        return source;
    }

    public void setSource(RequestSource source) {
        this.source = source;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
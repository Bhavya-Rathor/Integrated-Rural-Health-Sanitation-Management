package com.irhsn.medicine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;   // a date without time, like 2027-03-31

// Hey Spring, make a table out of this class too,
// and call it "stock".
// One row = one batch of one medicine sitting at one health centre.
@Entity
@Table(name = "stock")
public class Stock {

    // Auto-numbered ID, same as in Medicine.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== THE LINK TO THE MEDICINE TABLE =====
    // "Which medicine is this batch of?"
    // @ManyToOne = many stock rows can point to the same one medicine.
    // @JoinColumn = in the database, store this link as a column
    // called "medicine_id" that holds the medicine's id number.
    // nullable = false = every batch must belong to some medicine.
    // In Java, I get the whole Medicine object, not just a number:
    // stock.getMedicine().getName() gives "Paracetamol 500 mg".
    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    // "Which health centre is this batch at?"
    // Just a number, because centres live in Bhavya's service
    // (a different database), so I can't link to them directly.
    @NotNull
    private Long centreId;

    // The batch/lot number printed on the box, like "B-101".
    @NotBlank
    @Column(length = 30)
    private String batchNo;

    // How many units are left in this batch right now.
    // Goes down every time medicine is given to a patient.
    // @Min(0) = can never go negative.
    @NotNull
    @Min(0)
    private Integer quantity;

    // When this batch expires. Used to:
    // 1) give out the oldest-expiring batch first (FEFO)
    // 2) show an "expiring soon" warning list
    @NotNull
    private LocalDate expiryDate;

    // The day this batch arrived at the centre.
    private LocalDate receivedDate;

    // Blank constructor for Spring, same as before.
    public Stock() {
    }

    // ===== GETTERS AND SETTERS =====
    // Generate them with Alt+Insert -> Getter and Setter -> Ctrl+A -> OK

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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }
}

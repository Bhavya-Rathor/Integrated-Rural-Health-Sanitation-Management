package com.irhsm.Resident.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "resident")
public class Resident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String fullName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Past
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^$|\\d{10}")
    private String phone;

        @NotBlank
    private String village;

    private String householdNo;

    @Column(unique = true)
    @Pattern(regexp = "^$|\\d{14}")
    private String abhaNumber;

    private LocalDate registeredOn;

    private boolean active = true;

    public Resident() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getHouseholdNo() {
        return householdNo;
    }

    public void setHouseholdNo(String householdNo) {
        this.householdNo = householdNo;
    }

    public String getAbhaNumber() {
        return abhaNumber;
    }

    public void setAbhaNumber(String abhaNumber) {
        this.abhaNumber = abhaNumber;
    }

    public LocalDate getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDate registeredOn) {
        this.registeredOn = registeredOn;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
}
package com.irhsm.Resident.dto;

import com.irhsm.Resident.entity.Resident;

import java.time.LocalDate;

public record ResidentDto(Long id, String fullName, String gender, LocalDate dateOfBirth,
                          String phone, String village, String householdNo, String abhaNumber) {

    public static ResidentDto from(Resident r) {
        return new ResidentDto(
                r.getId(),
                r.getFullName(),
                // r.getAge(),
                r.getGender() != null ? r.getGender().name() : null,
                r.getDateOfBirth(),
                r.getPhone(),\
                r.getVillage(),
                r.getHouseholdNo(),
                r.getAbhaNumber());
    }
}
package com.irhsm.Resident.dto;

import com.irhsm.Resident.entity.MedicalInfo;

import java.time.LocalDate;

public record MedicalInfoDto(Long residentId, String bloodGroup, Double heightCm, Double weightKg,
                             String allergies, String chronicConditions, String currentMedications,
                             LocalDate lastUpdated) {

    public static MedicalInfoDto from(MedicalInfo m) {
        return new MedicalInfoDto(
                m.getResident().getId(),
                m.getBloodGroup(),
                m.getHeightCm(),
                m.getWeightKg(),
                m.getAllergies(),
                m.getChronicConditions(),
                m.getCurrentMedications(),
                m.getLastUpdated());
    }
}
package com.irhsm.Resident.service;

import com.irhsm.Resident.entity.MedicalInfo;
import com.irhsm.Resident.entity.Resident;
import com.irhsm.Resident.exception.ResidentNotFoundException;
import com.irhsm.Resident.repository.MedicalInfoRepository;
import com.irhsm.Resident.repository.ResidentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ResidentService {

    private final ResidentRepository residentRepository;
    private final MedicalInfoRepository medicalInfoRepository;

    public ResidentService(ResidentRepository residentRepository,
                           MedicalInfoRepository medicalInfoRepository) {
        this.residentRepository = residentRepository;
        this.medicalInfoRepository = medicalInfoRepository;
    }

    public List<Resident> search(String q, String village) {
        boolean hasQ = q != null && !q.isBlank();
        boolean hasVillage = village != null && !village.isBlank();

        if (hasQ && hasVillage) {
            return residentRepository.findByActiveTrueAndVillageAndFullNameContainingIgnoreCase(village, q.trim());
        }
        if (hasVillage) {
            return residentRepository.findByActiveTrueAndVillage(village);
        }
        if (hasQ) {
            return residentRepository.findByActiveTrueAndFullNameContainingIgnoreCase(q.trim());
        }
        return residentRepository.findByActiveTrue();
    }

    public Resident getById(Long id) {
        return residentRepository.findById(id)
                .filter(Resident::isActive)
                .orElseThrow(() -> new ResidentNotFoundException(id));
    }

    @Transactional
    public Resident create(Resident r) {
        r.setId(null);
        blankAbhaToNull(r);
        if (r.getAbhaNumber() != null && residentRepository.existsByAbhaNumber(r.getAbhaNumber())) {
            throw new IllegalArgumentException("ABHA number already registered");
        }
        r.setRegisteredOn(LocalDate.now());
        r.setActive(true);
        return residentRepository.save(r);
    }

    @Transactional
    public Resident update(Long id, Resident form) {
        Resident existing = getById(id);
        blankAbhaToNull(form);
        boolean abhaChanged = form.getAbhaNumber() != null
                && !form.getAbhaNumber().equals(existing.getAbhaNumber());
        if (abhaChanged && residentRepository.existsByAbhaNumber(form.getAbhaNumber())) {
            throw new IllegalArgumentException("ABHA number already registered");
        }
        existing.setFullName(form.getFullName());
        existing.setGender(form.getGender());
        existing.setDateOfBirth(form.getDateOfBirth());
        existing.setPhone(form.getPhone());
        existing.setVillage(form.getVillage());
        existing.setHouseholdNo(form.getHouseholdNo());
        existing.setAbhaNumber(form.getAbhaNumber());
        return residentRepository.save(existing);
    }

    @Transactional
    public void deactivate(Long id) {
        Resident r = getById(id);
        r.setActive(false); // soft delete: other services still point to this id
        residentRepository.save(r);
    }

    public MedicalInfo getMedicalInfo(Long residentId) {
        Resident r = getById(residentId);
        return medicalInfoRepository.findByResidentId(residentId).orElseGet(() -> {
            MedicalInfo empty = new MedicalInfo();
            empty.setResident(r);
            return empty;
        });
    }

    @Transactional
    public MedicalInfo saveMedicalInfo(Long residentId, MedicalInfo form) {
        MedicalInfo m = getMedicalInfo(residentId);
        m.setBloodGroup(form.getBloodGroup());
        m.setHeightCm(form.getHeightCm());
        m.setWeightKg(form.getWeightKg());
        m.setAllergies(form.getAllergies());
        m.setChronicConditions(form.getChronicConditions());
        m.setCurrentMedications(form.getCurrentMedications());
        m.setLastUpdated(LocalDate.now());
        return medicalInfoRepository.save(m);
    }

    // Two empty strings would break the unique ABHA column, so store null instead
    private void blankAbhaToNull(Resident r) {
        if (r.getAbhaNumber() != null && r.getAbhaNumber().isBlank()) {
            r.setAbhaNumber(null);
        }
    }
}
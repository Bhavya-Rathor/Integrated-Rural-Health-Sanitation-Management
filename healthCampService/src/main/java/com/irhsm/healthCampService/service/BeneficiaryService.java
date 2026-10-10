package com.irhsm.healthCampService.service;

import com.irhsm.healthCampService.dto.BeneficiaryForm;
import com.irhsm.healthCampService.entity.*;
import com.irhsm.healthCampService.repository.CampBeneficiaryRepository;
import com.irhsm.healthCampService.repository.CampStaffRepository;
import com.irhsm.healthCampService.dto.StaffForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Who comes to a camp: residents (beneficiaries) and staff
@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final CampBeneficiaryRepository beneficiaryRepository;
    private final CampStaffRepository staffRepository;
    private final CampService campService;

    public List<CampBeneficiary> findByCamp(Long campId) {
        return beneficiaryRepository.findByCampIdOrderByResidentNameAsc(campId);
    }

    public CampBeneficiary getById(Long id) {
        return beneficiaryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Beneficiary " + id + " not found"));
    }

    // Register a resident for a camp. The same resident cannot register twice (409).
    @Transactional
    public CampBeneficiary register(Long campId, BeneficiaryForm form) {
        HealthCamp camp = campService.getById(campId);
        if (camp.getStatus() == CampStatus.CANCELLED || camp.getStatus() == CampStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot register for a " + camp.getStatus() + " camp");
        }
        if (beneficiaryRepository.existsByCampIdAndResidentId(campId, form.getResidentId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Resident " + form.getResidentId() + " is already registered for this camp");
        }
        CampBeneficiary b = new CampBeneficiary();
        b.setCamp(camp);
        b.setResidentId(form.getResidentId());
        b.setResidentName(form.getResidentName().trim());
        b.setVillage(form.getVillage() == null || form.getVillage().isBlank()
                ? camp.getVillage() : form.getVillage().trim());
        b.setAge(form.getAge());
        b.setGender(form.getGender());
        return beneficiaryRepository.save(b);
    }

    @Transactional
    public CampBeneficiary markAttendance(Long beneficiaryId, Attendance attendance) {
        CampBeneficiary b = getById(beneficiaryId);
        b.setAttendance(attendance);
        return beneficiaryRepository.save(b);
    }

    public List<CampStaff> staffOf(Long campId) {
        return staffRepository.findByCampId(campId);
    }

    @Transactional
    public CampStaff addStaff(Long campId, StaffForm form) {
        CampStaff s = new CampStaff();
        s.setCamp(campService.getById(campId));
        s.setWorkerId(form.getWorkerId());
        s.setWorkerName(form.getWorkerName().trim());
        s.setRole(form.getRole());
        s.setDuty(form.getDuty());
        return staffRepository.save(s);
    }
}

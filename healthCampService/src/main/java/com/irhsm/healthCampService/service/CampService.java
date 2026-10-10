package com.irhsm.healthCampService.service;

import com.irhsm.healthCampService.entity.CampStatus;
import com.irhsm.healthCampService.entity.HealthCamp;
import com.irhsm.healthCampService.repository.HealthCampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

// Business rules for camps: search, add/edit, change status
@Service
@RequiredArgsConstructor   // Lombok: constructor for the final fields -> Spring injects them
public class CampService {

    private final HealthCampRepository campRepository;

    // Filter by village and/or status (blank = no filter)
    public List<HealthCamp> search(String village, CampStatus status) {
        boolean hasVillage = village != null && !village.isBlank();
        if (hasVillage && status != null)
            return campRepository.findByVillageIgnoreCaseAndStatusOrderByCampDateDesc(village, status);
        if (hasVillage) return campRepository.findByVillageIgnoreCaseOrderByCampDateDesc(village);
        if (status != null) return campRepository.findByStatusOrderByCampDateDesc(status);
        return campRepository.findAllByOrderByCampDateDesc();
    }

    public List<HealthCamp> findAll() {
        return campRepository.findAllByOrderByCampDateDesc();
    }

    // 404 if the id does not exist
    public HealthCamp getById(Long id) {
        return campRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Camp " + id + " not found"));
    }

    // Add (id == null) or edit (id given)
    @Transactional
    public HealthCamp save(HealthCamp camp) {
        if (camp.getStartTime() != null && camp.getEndTime() != null
                && camp.getEndTime().isBefore(camp.getStartTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End time is before start time");
        }
        if (camp.getId() != null) {
            HealthCamp existing = getById(camp.getId());
            if (camp.getStatus() == null) camp.setStatus(existing.getStatus());
        }
        if (camp.getStatus() == null) camp.setStatus(CampStatus.PLANNED);
        return campRepository.save(camp);
    }

    @Transactional
    public HealthCamp changeStatus(Long id, CampStatus status) {
        HealthCamp camp = getById(id);
        camp.setStatus(status);
        return campRepository.save(camp);
    }

    public List<String> villages() {
        return campRepository.findVillages();
    }

    public long countAll() {
        return campRepository.count();
    }

    // PLANNED camps from today onwards
    public long countUpcoming() {
        return campRepository.countByStatusAndCampDateGreaterThanEqual(CampStatus.PLANNED, LocalDate.now());
    }
}

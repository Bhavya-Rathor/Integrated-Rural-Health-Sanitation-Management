package com.irhsm.sanitationservice.service;

import com.irhsm.sanitationservice.entity.SanitationFacility;
import com.irhsm.sanitationservice.repository.SanitationFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class FacilityService {
    private final SanitationFacilityRepository facilityRepository;

    // all facilities, or only one village is given
    public List<SanitationFacility> findAll(String village){
        if( village == null || village.isBlank()) {
            return facilityRepository.findAll();
        }
        return facilityRepository.findByVillageIgnoreCase(village);
    }

    public Optional<SanitationFacility> findById(Long id){
        return facilityRepository.findById(id);
    }

    public SanitationFacility create(SanitationFacility facility){
        facility.setId(null);  // a new record must not overwrite an existing id
        return facilityRepository.save(facility);
    }

    public Optional<SanitationFacility> update(Long id, SanitationFacility incoming){
        return facilityRepository.findById(id).map(existing -> {
            existing.setName(incoming.getName());
            existing.setType(incoming.getType());
            existing.setVillage(incoming.getVillage());
            existing.setLocationDesc(incoming.getLocationDesc());
            existing.setCapacity(incoming.getCapacity());
            existing.setCondition(incoming.getCondition());
            existing.setLastCleanedOn(incoming.getLastCleanedOn());
            existing.setCaretakerName(incoming.getCaretakerName());
            return facilityRepository.save(existing);
        });

    }

    public  boolean delete(Long id) {
        if(!facilityRepository.existsById(id)) {
            return false;
        }
        facilityRepository.deleteById(id);
        return true;
    }
}

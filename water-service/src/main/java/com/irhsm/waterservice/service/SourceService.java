package com.irhsm.waterservice.service;

import com.irhsm.waterservice.entity.WaterSource;
import com.irhsm.waterservice.repository.WaterSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SourceService {

    private final WaterSourceRepository sourceRepository;

    public List<WaterSource> findAll(String village) {
        return (village == null || village.isBlank())
                ? sourceRepository.findAll()
                : sourceRepository.findByVillageIgnoreCase(village);
    }

    public Optional<WaterSource> findById(Long id) {
        return sourceRepository.findById(id);
    }

    public WaterSource create(WaterSource source) {
        source.setId(null);
        return sourceRepository.save(source);
    }

    public Optional<WaterSource> update(Long id, WaterSource incoming) {
        return sourceRepository.findById(id).map(existing -> {
            existing.setName(incoming.getName());
            existing.setType(incoming.getType());
            existing.setVillage(incoming.getVillage());
            existing.setLocationDesc(incoming.getLocationDesc());
            existing.setHouseholdsServed(incoming.getHouseholdsServed());
            existing.setFunctional(incoming.getFunctional());
            return sourceRepository.save(existing);
        });
    }
}
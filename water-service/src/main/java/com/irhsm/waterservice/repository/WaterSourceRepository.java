package com.irhsm.waterservice.repository;

import com.irhsm.waterservice.entity.WaterSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaterSourceRepository extends JpaRepository<WaterSource, Long> {
    List<WaterSource> findByVillageIgnoreCase(String village);
}
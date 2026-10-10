package com.irhsm.waterservice.repository;

import com.irhsm.waterservice.entity.WaterQualityTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaterQualityTestRepository extends JpaRepository<WaterQualityTest, Long> {
    List<WaterQualityTest> findAllByOrderByTestDateDesc();
    List<WaterQualityTest> findBySourceIdOrderByTestDateDesc(Long sourceId);
}
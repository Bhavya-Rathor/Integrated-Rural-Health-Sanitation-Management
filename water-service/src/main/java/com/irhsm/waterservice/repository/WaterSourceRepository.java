package com.irhsm.waterservice.repository;

import com.irhsm.waterservice.entity.WaterSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WaterSourceRepository extends JpaRepository<WaterSource, Long> {
    List<WaterSource> findByVillageIgnoreCase(String village);

    @Query("select distinct s.village from WaterSource s order by s.village")
    List<String> findDistinctVillages();
}
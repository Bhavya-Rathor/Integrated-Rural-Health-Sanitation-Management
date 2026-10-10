package com.irhsm.Resident.repository;

import com.irhsm.Resident.entity.FollowUpStatus;
import com.irhsm.Resident.entity.VisitRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VisitRecordRepository extends JpaRepository<VisitRecord, Long> {

    List<VisitRecord> findByResidentIdOrderByVisitDateDesc(Long residentId);

    List<VisitRecord> findByFollowUpStatusInAndFollowUpDateLessThanEqual(
            List<FollowUpStatus> statuses, LocalDate date);

    long countByVisitDateGreaterThanEqual(LocalDate date);
}
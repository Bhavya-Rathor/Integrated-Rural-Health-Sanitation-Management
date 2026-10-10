package com.irhsm.sanitationservice.repository;

import com.irhsm.sanitationservice.entity.Complaint;
import com.irhsm.sanitationservice.enums.WorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // WHERE status <> ? ORDER BY reported_on DESC
    List<Complaint> findByStatusNotOrderByReportedOnDesc(WorkStatus status);

    long countByStatusNot(WorkStatus status);

    // facility.id → Spring follows the relationship for you
    List<Complaint> findByFacilityId(Long facilityId);

    List<Complaint> findAllByOrderByReportedOnDesc();

    List<Complaint> findByStatusOrderByReportedOnDesc(WorkStatus status);
}
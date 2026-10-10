package com.irhsm.waterservice.repository;

import com.irhsm.waterservice.entity.PublicHealthIssue;
import com.irhsm.waterservice.enums.WorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicHealthIssueRepository extends JpaRepository<PublicHealthIssue, Long> {
    List<PublicHealthIssue> findAllByOrderByReportedOnDesc();
    List<PublicHealthIssue> findByStatusNotOrderByReportedOnDesc(WorkStatus status);
    long countByStatusNot(WorkStatus status);
}
package com.irhsm.waterservice.repository;

import com.irhsm.waterservice.entity.CorrectiveAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CorrectiveActionRepository extends JpaRepository<CorrectiveAction, Long> {
    List<CorrectiveAction> findByIssueIdOrderByActionDateDesc(Long issueId);
}
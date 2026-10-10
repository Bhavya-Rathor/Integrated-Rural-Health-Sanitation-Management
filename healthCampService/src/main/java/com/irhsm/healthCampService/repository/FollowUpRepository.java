package com.irhsm.healthCampService.repository;

import com.irhsm.healthCampService.entity.FollowUp;
import com.irhsm.healthCampService.entity.FollowUpStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {
    List<FollowUp> findByStatusOrderByDueDateAsc(FollowUpStatus status);
    List<FollowUp> findAllByOrderByDueDateAsc();
    long countByStatus(FollowUpStatus status);
}

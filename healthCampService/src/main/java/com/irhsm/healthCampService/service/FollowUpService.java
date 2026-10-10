package com.irhsm.healthCampService.service;

import com.irhsm.healthCampService.entity.FollowUp;
import com.irhsm.healthCampService.entity.FollowUpStatus;
import com.irhsm.healthCampService.repository.FollowUpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

// Follow-ups: the "pending actions" of this service
@Service
@RequiredArgsConstructor
public class FollowUpService {

    private final FollowUpRepository followUpRepository;

    public List<FollowUp> pending() {
        return followUpRepository.findByStatusOrderByDueDateAsc(FollowUpStatus.PENDING);
    }

    public List<FollowUp> search(FollowUpStatus status) {
        return status == null
                ? followUpRepository.findAllByOrderByDueDateAsc()
                : followUpRepository.findByStatusOrderByDueDateAsc(status);
    }

    public List<FollowUp> findByCamp(Long campId) {
        return followUpRepository.findByScreeningBeneficiaryCampIdOrderByDueDateAsc(campId);
    }

    public long countPending() {
        return followUpRepository.countByStatus(FollowUpStatus.PENDING);
    }

    // Used by the dashboard (PUT /api/camps/{id}/status) and the follow-ups page.
    // Rule: only a PENDING follow-up can be closed, and it must move to DONE, REFERRED or MISSED.
    @Transactional
    public FollowUp updateStatus(Long id, String statusText, String remarks) {
        FollowUpStatus newStatus;
        try {
            newStatus = FollowUpStatus.valueOf(statusText.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown status: " + statusText + " (use DONE, REFERRED or MISSED)");
        }
        FollowUp f = followUpRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Follow-up " + id + " not found"));

        if (f.getStatus() != FollowUpStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Follow-up " + id + " is already " + f.getStatus());
        }
        if (newStatus == FollowUpStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Follow-up " + id + " is already PENDING");
        }
        f.setStatus(newStatus);
        f.setOutcome(remarks);
        f.setClosedOn(LocalDate.now());
        return followUpRepository.save(f);
    }
}

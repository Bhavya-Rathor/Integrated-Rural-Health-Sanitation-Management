package com.irhsm.healthCampService.controller.api;

import com.irhsm.healthCampService.dto.PendingItemDto;
import com.irhsm.healthCampService.dto.StatusUpdateDto;
import com.irhsm.healthCampService.dto.SummaryDto;
import com.irhsm.healthCampService.service.CampService;
import com.irhsm.healthCampService.service.FollowUpService;
import com.irhsm.healthCampService.service.ScreeningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// The 3 URLs the Community Health Dashboard calls (same in every service)
@RestController
@RequestMapping("/api/camps")
@RequiredArgsConstructor
public class CampIntegrationController {

    private final CampService campService;
    private final FollowUpService followUpService;
    private final ScreeningService screeningService;

    // GET /api/camps/summary -> {"total":4,"pending":3,"upcoming":2,"screened":6,"abnormal":4}
    @GetMapping("/summary")
    public SummaryDto summary() {
        return new SummaryDto(
                campService.countAll(),
                followUpService.countPending(),
                campService.countUpcoming(),
                screeningService.countAll(),
                screeningService.countNotNormal());
    }

    // GET /api/camps/pending -> PENDING follow-ups in the shared 7-field format
    @GetMapping("/pending")
    public List<PendingItemDto> pending() {
        return followUpService.pending().stream().map(PendingItemDto::from).toList();
    }

    // PUT /api/camps/{id}/status  body {"status":"REFERRED","remarks":"..."}  ({id} = follow-up id)
    @PutMapping("/{id}/status")
    public PendingItemDto updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateDto body) {
        return PendingItemDto.from(followUpService.updateStatus(id, body.status(), body.remarks()));
    }
}

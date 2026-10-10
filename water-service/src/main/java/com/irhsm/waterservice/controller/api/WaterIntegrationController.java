package com.irhsm.waterservice.controller.api;

import com.irhsm.waterservice.dto.PendingItemDto;
import com.irhsm.waterservice.dto.StatusUpdateRequest;
import com.irhsm.waterservice.dto.SummaryDto;
import com.irhsm.waterservice.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// The three endpoints Tvisha's dashboard calls
@RestController
@RequestMapping("/api/water")
@RequiredArgsConstructor
public class WaterIntegrationController {

    private final IssueService issueService;

    @GetMapping("/summary")
    public SummaryDto summary() {
        return new SummaryDto(issueService.countAll(), issueService.countOpen());
    }

    @GetMapping("/pending")
    public List<PendingItemDto> pending() {
        return issueService.findOpen().stream().map(PendingItemDto::from).toList();
    }

    @PutMapping("/{id}/status")
    public PendingItemDto updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest req) {
        return PendingItemDto.from(issueService.updateStatus(id, req.status(), req.remarks()));
    }
}
package com.irhsm.sanitationservice.controller.api;

import com.irhsm.sanitationservice.dto.PendingItemDto;
import com.irhsm.sanitationservice.dto.StatusUpdateRequest;
import com.irhsm.sanitationservice.dto.SummaryDto;
import com.irhsm.sanitationservice.service.ComplaintService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sanitation")
@RequiredArgsConstructor
public class SanitationIntegrationController {

    private final ComplaintService complaintService;

    @GetMapping("/summary")
    public SummaryDto summary() {
        return new SummaryDto(complaintService.countAll(), complaintService.countOpen());
    }

    @GetMapping("/pending")
    public List<PendingItemDto> pending() {
        return complaintService.findOpen().stream()
                .map(PendingItemDto::from)
                .toList();
    }

    @PutMapping("/{id}/status")
    public PendingItemDto updateStatus(@PathVariable Long id,
                                       @Valid @RequestBody StatusUpdateRequest req) {
        return PendingItemDto.from(complaintService.updateStatus(id, req.status(), req.remarks()));
    }
}
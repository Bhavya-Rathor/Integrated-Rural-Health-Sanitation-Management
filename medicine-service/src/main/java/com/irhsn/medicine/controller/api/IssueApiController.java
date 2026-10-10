package com.irhsn.medicine.controller.api;

import com.irhsn.medicine.dto.IssueRequestDto;
import com.irhsn.medicine.entity.IssueRecord;
import com.irhsn.medicine.service.IssueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Endpoints for giving medicine to patients.
@RestController
@RequestMapping("/api/medicines")
public class IssueApiController {

    private final IssueService issueService;

    public IssueApiController(IssueService issueService) {
        this.issueService = issueService;
    }

    // POST /api/medicines/issue
    // Called by Bhavya's Appointment service after a consultation.
    // Takes from the oldest-expiry batches first (FEFO),
    // returns one IssueRecord per batch used.
    // 409 if not enough stock, 404 if the medicine doesn't exist.
    @PostMapping("/issue")
    @ResponseStatus(HttpStatus.CREATED)
    public List<IssueRecord> issue(@Valid @RequestBody IssueRequestDto request) {
        return issueService.issue(
                request.medicineId(),
                request.centreId(),
                request.quantity(),
                request.residentId(),
                request.appointmentId(),
                request.issuedBy());
    }

    // GET /api/medicines/issues?residentId=5
    // A patient's medicine history, newest first.
    @GetMapping("/issues")
    public List<IssueRecord> history(@RequestParam Long residentId) {
        return issueService.historyForResident(residentId);
    }
}
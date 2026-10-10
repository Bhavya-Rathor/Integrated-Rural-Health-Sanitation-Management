package com.irhsm.waterservice.controller.api;

import com.irhsm.waterservice.dto.CorrectiveActionRequest;
import com.irhsm.waterservice.entity.CorrectiveAction;
import com.irhsm.waterservice.entity.PublicHealthIssue;
import com.irhsm.waterservice.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/water/issues")
@RequiredArgsConstructor
public class IssueApiController {

    private final IssueService issueService;

    @GetMapping
    public List<PublicHealthIssue> getAll() {
        return issueService.findAll();
    }

    @GetMapping("/{id}")
    public PublicHealthIssue getOne(@PathVariable Long id) {
        return issueService.findById(id);
    }

    @GetMapping("/{id}/actions")
    public List<CorrectiveAction> getActions(@PathVariable Long id) {
        return issueService.findActions(id);
    }

    @PostMapping("/{id}/actions")
    public ResponseEntity<CorrectiveAction> addAction(@PathVariable Long id,
                                                      @Valid @RequestBody CorrectiveActionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(issueService.addAction(id, req));
    }
}
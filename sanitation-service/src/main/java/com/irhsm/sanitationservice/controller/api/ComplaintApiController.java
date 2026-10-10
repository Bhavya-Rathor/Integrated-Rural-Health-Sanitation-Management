package com.irhsm.sanitationservice.controller.api;

import com.irhsm.sanitationservice.dto.ComplaintRequest;
import com.irhsm.sanitationservice.entity.Complaint;
import com.irhsm.sanitationservice.service.ComplaintService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sanitation/complaints")
@RequiredArgsConstructor
public class ComplaintApiController {

    private final ComplaintService complaintService;

    @GetMapping
    public List<Complaint> getAll() {
        return complaintService.findAll();
    }

    @GetMapping("/{id}")
    public Complaint getOne(@PathVariable Long id) {
        return complaintService.findById(id);   // a missing id throws → Spring returns 404
    }

    @PostMapping
    public ResponseEntity<Complaint> create(@Valid @RequestBody ComplaintRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(complaintService.create(req));
    }
}
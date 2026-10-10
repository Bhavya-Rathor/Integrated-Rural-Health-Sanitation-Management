package com.irhsm.Resident.controller;

import com.irhsm.Resident.dto.MedicalInfoDto;
import com.irhsm.Resident.dto.PendingItemDto;
import com.irhsm.Resident.dto.ResidentDto;
import com.irhsm.Resident.dto.StatusUpdateRequest;
import com.irhsm.Resident.dto.VisitDto;
import com.irhsm.Resident.entity.Resident;
import com.irhsm.Resident.service.ResidentService;
import com.irhsm.Resident.service.VisitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/residents")
public class ResidentApiController {

    private final ResidentService residentService;
    private final VisitService visitService;

    public ResidentApiController(ResidentService residentService, VisitService visitService) {
        this.residentService = residentService;
        this.visitService = visitService;
    }

    // GET /api/residents?q=lak&village=Katpadi   (used by Health Camp)
    @GetMapping
    public List<ResidentDto> list(@RequestParam(required = false) String q,
                                  @RequestParam(required = false) String village) {
        return residentService.search(q, village).stream().map(ResidentDto::from).toList();
    }

    //team contract endpoints (used by Dashboard)

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        return visitService.getSummary();
    }

    @GetMapping("/pending")
    public List<PendingItemDto> pending() {
        return visitService.getPendingFollowUps();
    }

    // id here is the VISIT id from /pending, not the resident id
    @PutMapping("/{id}/status")
    public PendingItemDto updateStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest body) {
        return visitService.updateFollowUpStatus(id, body.status());
    }

    // single resident (used by Appointment)

    @GetMapping("/{id}")
    public ResidentDto get(@PathVariable Long id) {
        return ResidentDto.from(residentService.getById(id));
    }

    @GetMapping("/{id}/medical")
    public MedicalInfoDto medical(@PathVariable Long id) {
        return MedicalInfoDto.from(residentService.getMedicalInfo(id));
    }

    @PostMapping
    public ResponseEntity<ResidentDto> create(@Valid @RequestBody Resident r) {
        Resident saved = residentService.create(r);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResidentDto.from(saved));
    }

    @PutMapping("/{id}")
    public ResidentDto update(@PathVariable Long id, @Valid @RequestBody Resident r) {
        return ResidentDto.from(residentService.update(id, r));
    }

    // visits (Appointment POSTs here after a consultation)

    @GetMapping("/{id}/visits")
    public List<VisitDto> visits(@PathVariable Long id) {
        return visitService.getHistory(id).stream().map(VisitDto::from).toList();
    }

    @PostMapping("/{id}/visits")
    public ResponseEntity<VisitDto> addVisit(@PathVariable Long id, @RequestBody VisitDto body) {
        var saved = visitService.addVisit(id, body.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(VisitDto.from(saved));
    }
}
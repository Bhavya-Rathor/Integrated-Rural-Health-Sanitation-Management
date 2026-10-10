package com.irhsn.medicine.controller.api;

import com.irhsn.medicine.dto.ManualRequestDto;
import com.irhsn.medicine.dto.PendingItemDto;
import com.irhsn.medicine.dto.StatusUpdateDto;
import com.irhsn.medicine.entity.ReplenishmentRequest;
import com.irhsn.medicine.enums.RequestStatus;
import com.irhsn.medicine.service.MedicineService;
import com.irhsn.medicine.service.ReplenishmentService;
import com.irhsn.medicine.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Restock requests + the 3 endpoints Tvisha's dashboard depends on.
@RestController
@RequestMapping("/api/medicines")
public class ReplenishmentApiController {

    private final ReplenishmentService replenishmentService;
    private final MedicineService medicineService;
    private final StockService stockService;

    public ReplenishmentApiController(ReplenishmentService replenishmentService,
                                      MedicineService medicineService,
                                      StockService stockService) {
        this.replenishmentService = replenishmentService;
        this.medicineService = medicineService;
        this.stockService = stockService;
    }

    // ===== TEAM CONTRACT 1: GET /api/medicines/summary =====
    // Numbers for the dashboard tiles.
    // "total" and "pending" are the team-agreed keys;
    // "lowStock" and "expiringSoon" are extras the dashboard can use or ignore.
    @GetMapping("/summary")
    public Map<String, Long> summary() {
        return Map.of(
                "total", (long) medicineService.findAll(null, null).size(), // medicines in catalogue
                "pending", replenishmentService.countByStatus(RequestStatus.PENDING),
                "lowStock", stockService.countLowStock(),
                "expiringSoon", (long) stockService.expiringWithin(30).size());
    }

    // ===== TEAM CONTRACT 2: GET /api/medicines/pending =====
    // Turn each PENDING request into the shared 7-field format.
    @GetMapping("/pending")
    public List<PendingItemDto> pending() {
        return replenishmentService.findByStatus(RequestStatus.PENDING).stream()
                .map(r -> new PendingItemDto(
                        "medicine",
                        r.getId(),
                        "Restock " + r.getMedicine().getName() + " at " + r.getVillage(),
                        r.getVillage(),
                        r.getStatus().name(),
                        r.getPriority().name(),
                        r.getCreatedAt().toLocalDate()))
                .toList();
    }

    // ===== TEAM CONTRACT 3: PUT /api/medicines/{id}/status =====
    // The officer clicks Approve/Reject on the dashboard -> Tvisha calls this.
    // {id} here is the REQUEST id (the same id that /pending gave out).
    // 400 if the move is not allowed (e.g. PENDING -> RECEIVED).
    @PutMapping("/{id}/status")
    public ReplenishmentRequest updateStatus(@PathVariable Long id,
                                             @Valid @RequestBody StatusUpdateDto body) {
        return replenishmentService.updateStatus(id, body.status(), body.remarks());
    }

    // GET /api/medicines/requests            -> all requests
    // GET /api/medicines/requests?status=APPROVED -> only approved
    @GetMapping("/requests")
    public List<ReplenishmentRequest> list(@RequestParam(required = false) RequestStatus status) {
        return replenishmentService.findByStatus(status);
    }

    // POST /api/medicines/requests
    // A health worker reports low stock by hand (the problem statement's example).
    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ReplenishmentRequest createManual(@Valid @RequestBody ManualRequestDto body) {
        return replenishmentService.createManual(
                body.medicineId(), body.centreId(), body.requestedQty(), body.remarks());
    }
}
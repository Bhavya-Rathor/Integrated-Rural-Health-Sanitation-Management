package com.irhsn.medicine.controller.api;

import com.irhsn.medicine.entity.Medicine;
import com.irhsn.medicine.enums.DosageForm;
import com.irhsn.medicine.service.MedicineService;
import com.irhsn.medicine.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// @RestController = every method returns JSON (data), not a web page.
// @RequestMapping = all URLs here start with /api/medicines
@RestController
@RequestMapping("/api/medicines")
public class MedicineApiController {

    private final MedicineService medicineService;
    private final StockService stockService;

    // Spring passes the services in (constructor injection).
    public MedicineApiController(MedicineService medicineService, StockService stockService) {
        this.medicineService = medicineService;
        this.stockService = stockService;
    }

    // GET /api/medicines
    // GET /api/medicines?search=para
    // GET /api/medicines?form=TABLET
    // @RequestParam(required = false) = these are optional ?key=value extras in the URL.
    // Bhavya uses this for the medicine dropdown on his prescription form.
    @GetMapping
    public List<Medicine> list(@RequestParam(required = false) String search,
                               @RequestParam(required = false) DosageForm form) {
        return medicineService.findAll(search, form);
    }

    // GET /api/medicines/5
    // @PathVariable = take the {id} part of the URL as a value.
    @GetMapping("/{id}")
    public Medicine getOne(@PathVariable Long id) {
        return medicineService.getById(id);
    }

    // POST /api/medicines   (body = medicine JSON)
    // @RequestBody = read the JSON sent in the request into a Medicine object.
    // @Valid       = check @NotBlank, @Min etc. before saving.
    // @ResponseStatus(CREATED) = answer with 201 Created instead of 200.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Medicine create(@Valid @RequestBody Medicine medicine) {
        return medicineService.save(medicine);
    }

    // GET /api/medicines/5/availability?centreId=2
    // "How many units of medicine 5 are at centre 2?"
    // Bhavya checks this before prescribing.
    @GetMapping("/{id}/availability")
    public Map<String, Object> availability(@PathVariable Long id,
                                            @RequestParam Long centreId) {
        medicineService.getById(id); // 404 if the medicine doesn't exist
        int available = stockService.totalQuantity(id, centreId);
        return Map.of("medicineId", id, "centreId", centreId, "available", available);
    }
}
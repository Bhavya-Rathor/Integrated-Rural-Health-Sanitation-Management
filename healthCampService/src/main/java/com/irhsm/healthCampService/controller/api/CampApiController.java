package com.irhsm.healthCampService.controller.api;

import com.irhsm.healthCampService.entity.*;
import com.irhsm.healthCampService.service.BeneficiaryService;
import com.irhsm.healthCampService.service.CampService;
import com.irhsm.healthCampService.service.FollowUpService;
import com.irhsm.healthCampService.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Read-only REST API for other services (e.g. a resident's screening history)
@RestController
@RequestMapping("/api/camps")
@RequiredArgsConstructor
public class CampApiController {

    private final CampService campService;
    private final BeneficiaryService beneficiaryService;
    private final ScreeningService screeningService;
    private final FollowUpService followUpService;

    // GET /api/camps?village=Katpadi&status=PLANNED
    @GetMapping
    public List<HealthCamp> list(@RequestParam(required = false) String village,
                                 @RequestParam(required = false) CampStatus status) {
        return campService.search(village, status);
    }

    // GET /api/camps/1   (404 if missing)
    @GetMapping("/{id}")
    public HealthCamp one(@PathVariable Long id) {
        return campService.getById(id);
    }

    // GET /api/camps/1/beneficiaries
    @GetMapping("/{id}/beneficiaries")
    public List<CampBeneficiary> beneficiaries(@PathVariable Long id) {
        campService.getById(id);
        return beneficiaryService.findByCamp(id);
    }

    // GET /api/camps/screenings?residentId=2  -> a resident's screening history
    @GetMapping("/screenings")
    public List<Screening> screenings(@RequestParam Long residentId) {
        return screeningService.findByResident(residentId);
    }

    // GET /api/camps/followups?status=PENDING
    @GetMapping("/followups")
    public List<FollowUp> followUps(@RequestParam(required = false) FollowUpStatus status) {
        return followUpService.search(status);
    }
}

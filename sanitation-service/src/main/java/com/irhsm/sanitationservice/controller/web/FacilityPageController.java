package com.irhsm.sanitationservice.controller.web;

import com.irhsm.sanitationservice.dto.InspectionRequest;
import com.irhsm.sanitationservice.entity.SanitationFacility;
import com.irhsm.sanitationservice.enums.FacilityCondition;
import com.irhsm.sanitationservice.enums.FacilityType;
import com.irhsm.sanitationservice.service.ComplaintService;
import com.irhsm.sanitationservice.service.FacilityService;
import com.irhsm.sanitationservice.service.InspectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller                                   // returns template names, not JSON
@RequestMapping("/sanitation/facilities")
@RequiredArgsConstructor
public class FacilityPageController {

    private final FacilityService facilityService;
    private final InspectionService inspectionService;
    private final ComplaintService complaintService;

    // Methods annotated @ModelAttribute run before EVERY handler in this class,
    // so every template here can use ${types}, ${conditions} and ${villages}
    @ModelAttribute("types")
    public FacilityType[] types() { return FacilityType.values(); }

    @ModelAttribute("conditions")
    public FacilityCondition[] conditions() { return FacilityCondition.values(); }

    @ModelAttribute("villages")
    public List<String> villages() { return facilityService.villages(); }

    // GET /sanitation/facilities?village=Katpadi&condition=NEEDS_REPAIR
    @GetMapping
    public String list(@RequestParam(required = false) String village,
                       @RequestParam(required = false) FacilityCondition condition,
                       Model model) {
        model.addAttribute("facilities", facilityService.search(village, condition));
        model.addAttribute("selectedVillage", village);
        model.addAttribute("selectedCondition", condition);
        return "facilities/list";             // → templates/facilities/list.html
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("facility", new SanitationFacility());
        return "facilities/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("facility", findOr404(id));
        return "facilities/form";
    }

    // Handles both add and edit: a hidden id field tells them apart
    @PostMapping
    public String save(@Valid @ModelAttribute("facility") SanitationFacility facility,
                       BindingResult result,
                       RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "facilities/form";         // show the form again, with error messages
        }
        SanitationFacility saved = (facility.getId() == null)
                ? facilityService.create(facility)
                : facilityService.update(facility.getId(), facility)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        redirect.addFlashAttribute("message", "Facility saved");
        return "redirect:/sanitation/facilities/" + saved.getId();   // Post/Redirect/Get
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("facility", findOr404(id));
        model.addAttribute("inspections", inspectionService.findByFacility(id));
        model.addAttribute("complaints", complaintService.findByFacility(id));
        return "facilities/detail";
    }

    // The inspection form on the detail page posts here
    @PostMapping("/{id}/inspections")
    public String addInspection(@PathVariable Long id,
                                @Valid @ModelAttribute InspectionRequest req,
                                BindingResult result,
                                RedirectAttributes redirect) {
        if (result.hasErrors()) {
            redirect.addFlashAttribute("error",
                    "Inspection not saved: score must be 1–5 and the date cannot be in the future.");
        } else {
            inspectionService.create(id, req);
            redirect.addFlashAttribute("message", "Inspection added");
        }
        return "redirect:/sanitation/facilities/" + id;
    }

    private SanitationFacility findOr404(Long id) {
        return facilityService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Facility not found"));
    }
}
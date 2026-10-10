package com.irhsm.sanitationservice.controller.web;

import com.irhsm.sanitationservice.dto.ComplaintForm;
import com.irhsm.sanitationservice.entity.Complaint;
import com.irhsm.sanitationservice.entity.SanitationFacility;
import com.irhsm.sanitationservice.enums.Priority;
import com.irhsm.sanitationservice.enums.WorkStatus;
import com.irhsm.sanitationservice.service.ComplaintService;
import com.irhsm.sanitationservice.service.FacilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/sanitation/complaints")
@RequiredArgsConstructor
public class ComplaintPageController {

    private final ComplaintService complaintService;
    private final FacilityService facilityService;

    @ModelAttribute("statuses")
    public WorkStatus[] statuses() { return WorkStatus.values(); }

    @ModelAttribute("priorities")
    public Priority[] priorities() { return Priority.values(); }

    @ModelAttribute("facilities")
    public List<SanitationFacility> facilities() { return facilityService.findAll(null); }

    // GET /sanitation/complaints?status=OPEN
    @GetMapping
    public String list(@RequestParam(required = false) WorkStatus status, Model model) {
        model.addAttribute("complaints", complaintService.search(status));
        model.addAttribute("selectedStatus", status);
        return "complaints/list";
    }

    // GET /sanitation/complaints/new?facilityId=2  (facility pre-selected when coming from its page)
    @GetMapping("/new")
    public String newForm(@RequestParam(required = false) Long facilityId, Model model) {
        ComplaintForm form = new ComplaintForm();
        form.setFacilityId(facilityId);
        model.addAttribute("complaintForm", form);
        return "complaints/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("complaintForm") ComplaintForm form,
                       BindingResult result,
                       RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "complaints/form";
        }
        Complaint saved = complaintService.create(form.toRequest());
        redirect.addFlashAttribute("message", "Complaint #" + saved.getId() + " reported");
        return "redirect:/sanitation/facilities/" + form.getFacilityId();
    }
}
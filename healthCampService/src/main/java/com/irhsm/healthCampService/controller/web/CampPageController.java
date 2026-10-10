package com.irhsm.healthCampService.controller.web;

import com.irhsm.healthCampService.dto.BeneficiaryForm;
import com.irhsm.healthCampService.dto.ScreeningForm;
import com.irhsm.healthCampService.dto.StaffForm;
import com.irhsm.healthCampService.entity.*;
import com.irhsm.healthCampService.service.BeneficiaryService;
import com.irhsm.healthCampService.service.CampService;
import com.irhsm.healthCampService.service.FollowUpService;
import com.irhsm.healthCampService.service.ScreeningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Thymeleaf pages for camps: list, add/edit, detail (+ the small forms on the detail page)
@Controller
@RequestMapping("/camps")
@RequiredArgsConstructor
public class CampPageController {

    private final CampService campService;
    private final BeneficiaryService beneficiaryService;
    private final ScreeningService screeningService;
    private final FollowUpService followUpService;

    // Run before every handler here, so every template can use these lists
    @ModelAttribute("campTypes")   public CampType[] campTypes()     { return CampType.values(); }
    @ModelAttribute("statuses")    public CampStatus[] statuses()    { return CampStatus.values(); }

    // GET /camps?village=Katpadi&status=PLANNED
    @GetMapping
    public String list(@RequestParam(required = false) String village,
                       @RequestParam(required = false) CampStatus status,
                       Model model) {
        model.addAttribute("camps", campService.search(village, status));
        model.addAttribute("villages", campService.villages());
        model.addAttribute("selectedVillage", village);
        model.addAttribute("selectedStatus", status);
        return "camps/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("camp", new HealthCamp());
        return "camps/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("camp", campService.getById(id));
        return "camps/form";
    }

    // Add and edit both post here; the hidden id field tells them apart
    @PostMapping
    public String save(@Valid @ModelAttribute("camp") HealthCamp camp,
                       BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "camps/form";                       // show the form again with red errors
        }
        try {
            HealthCamp saved = campService.save(camp);
            redirect.addFlashAttribute("message", "Camp saved");
            return "redirect:/camps/" + saved.getId();  // Post -> Redirect -> Get
        } catch (ResponseStatusException e) {
            model.addAttribute("error", e.getReason());
            return "camps/form";
        }
    }

    // Camp detail: residents, staff, screenings, follow-ups + forms to add them
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("camp", campService.getById(id));
        model.addAttribute("beneficiaries", beneficiaryService.findByCamp(id));
        model.addAttribute("staff", beneficiaryService.staffOf(id));
        model.addAttribute("screenings", screeningService.findByCamp(id));
        model.addAttribute("followUps", followUpService.findByCamp(id));
        model.addAttribute("attendances", Attendance.values());
        return "camps/detail";
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam CampStatus status,
                               RedirectAttributes redirect) {
        campService.changeStatus(id, status);
        redirect.addFlashAttribute("message", "Camp marked " + status);
        return "redirect:/camps/" + id;
    }

    // ---------- Register a resident: GET shows the form, POST saves ----------
    @GetMapping("/{id}/beneficiaries/new")
    public String beneficiaryForm(@PathVariable Long id, Model model) {
        model.addAttribute("camp", campService.getById(id));
        model.addAttribute("beneficiaryForm", new BeneficiaryForm());
        return "camps/beneficiary-form";
    }

    @PostMapping("/{id}/beneficiaries")
    public String register(@PathVariable Long id,
                           @Valid @ModelAttribute("beneficiaryForm") BeneficiaryForm form,
                           BindingResult result, Model model, RedirectAttributes redirect) {
        if (!result.hasErrors()) {
            try {
                beneficiaryService.register(id, form);
                redirect.addFlashAttribute("message", form.getResidentName() + " registered");
                return "redirect:/camps/" + id;                 // Post -> Redirect -> Get
            } catch (ResponseStatusException e) {
                model.addAttribute("error", e.getReason());     // e.g. already registered
            }
        }
        model.addAttribute("camp", campService.getById(id));
        return "camps/beneficiary-form";                        // show the form again with errors
    }

    @PostMapping("/{id}/beneficiaries/{beneficiaryId}/attendance")
    public String attendance(@PathVariable Long id, @PathVariable Long beneficiaryId,
                             @RequestParam Attendance attendance, RedirectAttributes redirect) {
        beneficiaryService.markAttendance(beneficiaryId, attendance);
        redirect.addFlashAttribute("message", "Attendance updated");
        return "redirect:/camps/" + id;
    }

    // ---------- Add staff ----------
    @GetMapping("/{id}/staff/new")
    public String staffForm(@PathVariable Long id, Model model) {
        model.addAttribute("camp", campService.getById(id));
        model.addAttribute("staffForm", new StaffForm());
        model.addAttribute("roles", StaffRole.values());
        return "camps/staff-form";
    }

    @PostMapping("/{id}/staff")
    public String addStaff(@PathVariable Long id,
                           @Valid @ModelAttribute("staffForm") StaffForm form,
                           BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("camp", campService.getById(id));
            model.addAttribute("roles", StaffRole.values());
            return "camps/staff-form";
        }
        beneficiaryService.addStaff(id, form);
        redirect.addFlashAttribute("message", form.getWorkerName() + " added to the camp team");
        return "redirect:/camps/" + id;
    }

    // ---------- Record a screening ----------
    @GetMapping("/{id}/screenings/new")
    public String screeningForm(@PathVariable Long id, Model model) {
        model.addAttribute("screeningForm", new ScreeningForm());
        addScreeningLists(id, model);
        return "camps/screening-form";
    }

    @PostMapping("/{id}/screenings")
    public String screening(@PathVariable Long id,
                            @Valid @ModelAttribute("screeningForm") ScreeningForm form,
                            BindingResult result, Model model, RedirectAttributes redirect) {
        if (!result.hasErrors()) {
            try {
                screeningService.record(id, form);
                redirect.addFlashAttribute("message", form.getResult() == ScreeningResult.NORMAL
                        ? "Screening saved"
                        : "Screening saved - a follow-up was created for the health officer");
                return "redirect:/camps/" + id;
            } catch (ResponseStatusException e) {
                model.addAttribute("error", e.getReason());
            }
        }
        addScreeningLists(id, model);
        return "camps/screening-form";
    }

    // camp + dropdown lists the screening form needs
    private void addScreeningLists(Long id, Model model) {
        model.addAttribute("camp", campService.getById(id));
        model.addAttribute("beneficiaries", beneficiaryService.findByCamp(id));
        model.addAttribute("screeningTypes", ScreeningType.values());
        model.addAttribute("results", ScreeningResult.values());
    }

}

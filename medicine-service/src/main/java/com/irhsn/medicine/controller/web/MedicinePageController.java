package com.irhsn.medicine.controller.web;

import com.irhsn.medicine.entity.Medicine;
import com.irhsn.medicine.enums.DosageForm;
import com.irhsn.medicine.service.MedicineService;
import com.irhsn.medicine.service.ReplenishmentService;
import com.irhsn.medicine.service.StockService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/medicines")
public class MedicinePageController {

    private final MedicineService medicineService;
    private final StockService stockService;                 // NEW
    private final ReplenishmentService replenishmentService; // NEW

    public MedicinePageController(MedicineService medicineService,
                                  StockService stockService,
                                  ReplenishmentService replenishmentService) {
        this.medicineService = medicineService;
        this.stockService = stockService;
        this.replenishmentService = replenishmentService;
    }

    // GET /medicines -> list page with search and filter
    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       @RequestParam(required = false) DosageForm form,
                       Model model) {
        model.addAttribute("medicines", medicineService.findAll(search, form));
        model.addAttribute("forms", DosageForm.values());
        model.addAttribute("search", search);
        model.addAttribute("selectedForm", form);
        return "medicines/list";
    }

    // GET /medicines/new -> empty form for adding
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("medicine", new Medicine());
        model.addAttribute("forms", DosageForm.values());
        return "medicines/form";
    }

    // GET /medicines/5/edit -> same form, pre-filled
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("medicine", medicineService.getById(id));
        model.addAttribute("forms", DosageForm.values());
        return "medicines/form";
    }

    // POST /medicines/save -> add or edit
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("medicine") Medicine medicine,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("forms", DosageForm.values());
            return "medicines/form";
        }
        try {
            medicineService.save(medicine);
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("name", "duplicate", "A medicine with this name already exists");
            model.addAttribute("forms", DosageForm.values());
            return "medicines/form";
        }
        redirect.addFlashAttribute("message", "Medicine '" + medicine.getName() + "' saved");
        return "redirect:/medicines";
    }

    // ===== NEW: GET /medicines/5 -> detail page =====
    // Shows the medicine, all its batches (every centre) and its restock requests.
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("medicine", medicineService.getById(id));
        model.addAttribute("batches", stockService.findByMedicine(id));

        // This medicine's requests only (filter the full list by medicine id).
        model.addAttribute("requests", replenishmentService.findByStatus(null).stream()
                .filter(r -> r.getMedicine().getId().equals(id))
                .toList());

        // Dates so the page can flag expired / expiring-soon batches.
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("soon", LocalDate.now().plusDays(30));
        return "medicines/detail";
    }
}
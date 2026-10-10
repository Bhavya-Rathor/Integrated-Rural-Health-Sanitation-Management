package com.irhsm.HealthCentreService.controller;

import com.irhsm.HealthCentreService.entity.CentreStatus;
import com.irhsm.HealthCentreService.entity.CentreType;
import com.irhsm.HealthCentreService.entity.HealthCentre;
import com.irhsm.HealthCentreService.service.CentreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/centres")
@RequiredArgsConstructor
public class CentrePageController {

    private final CentreService centreService;

    @GetMapping
    public String list(@RequestParam(required = false) String village, Model model) {
        model.addAttribute("centres", centreService.searchByVillage(village));
        model.addAttribute("village", village);
        return "centres/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("centre", new HealthCentre());
        addOptions(model);
        return "centres/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("centre", centreService.getById(id));
        addOptions(model);
        return "centres/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("centre") HealthCentre centre,
                       BindingResult result, Model model) {
        if (result.hasErrors()) {
            addOptions(model);
            return "centres/form";
        }
        centreService.save(centre);
        return "redirect:/centres";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("centre", centreService.getById(id));
        model.addAttribute("facilities", centreService.getFacilities(id));
        model.addAttribute("workers", centreService.getActiveWorkers(id));
        return "centres/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        centreService.delete(id);
        return "redirect:/centres";
    }

    private void addOptions(Model model) {
        model.addAttribute("types", CentreType.values());
        model.addAttribute("statuses", CentreStatus.values());
    }
}
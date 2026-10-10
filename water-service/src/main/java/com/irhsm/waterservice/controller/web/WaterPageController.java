package com.irhsm.waterservice.controller.web;

import com.irhsm.waterservice.dto.CorrectiveActionRequest;
import com.irhsm.waterservice.dto.WaterTestRequest;
import com.irhsm.waterservice.entity.WaterQualityTest;
import com.irhsm.waterservice.entity.WaterSource;
import com.irhsm.waterservice.enums.SourceType;
import com.irhsm.waterservice.enums.TestResult;
import com.irhsm.waterservice.enums.WorkStatus;
import com.irhsm.waterservice.service.IssueService;
import com.irhsm.waterservice.service.SourceService;
import com.irhsm.waterservice.service.WaterTestService;
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

@Controller
@RequestMapping("/water")
@RequiredArgsConstructor
public class WaterPageController {

    private final SourceService sourceService;
    private final WaterTestService testService;
    private final IssueService issueService;

    @ModelAttribute("types")
    public SourceType[] types() { return SourceType.values(); }

    @ModelAttribute("statuses")
    public WorkStatus[] statuses() { return WorkStatus.values(); }

    @ModelAttribute("villages")
    public List<String> villages() { return sourceService.villages(); }

    @GetMapping({"", "/"})
    public String home() {
        return "redirect:/water/sources";
    }

    // ---------- Sources ----------

    @GetMapping("/sources")
    public String sources(@RequestParam(required = false) String village, Model model) {
        model.addAttribute("sources", sourceService.findAll(village));
        model.addAttribute("selectedVillage", village);
        return "sources/list";
    }

    @GetMapping("/sources/new")
    public String newSource(Model model) {
        model.addAttribute("source", new WaterSource());
        return "sources/form";
    }

    @GetMapping("/sources/{id}/edit")
    public String editSource(@PathVariable Long id, Model model) {
        model.addAttribute("source", findSource(id));
        return "sources/form";
    }

    @PostMapping("/sources")
    public String saveSource(@Valid @ModelAttribute("source") WaterSource source,
                             BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "sources/form";
        }
        WaterSource saved = (source.getId() == null)
                ? sourceService.create(source)
                : sourceService.update(source.getId(), source)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        redirect.addFlashAttribute("message", "Water source saved");
        return "redirect:/water/sources/" + saved.getId();
    }

    @GetMapping("/sources/{id}")
    public String sourceDetail(@PathVariable Long id, Model model) {
        model.addAttribute("source", findSource(id));
        model.addAttribute("tests", testService.findAll(id));
        return "sources/detail";
    }

    // The "record test" form on the source detail page
    @PostMapping("/sources/{id}/tests")
    public String addTest(@PathVariable Long id,
                          @Valid @ModelAttribute WaterTestRequest req,
                          BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            redirect.addFlashAttribute("error", "Test not saved: fill every value; pH must be 0–14 and the date not in the future.");
        } else {
            WaterQualityTest test = testService.create(req);
            if (test.getResult() == TestResult.UNSAFE) {
                redirect.addFlashAttribute("error",
                        "UNSAFE (" + test.getFailedParameters() + "). A public-health issue was opened.");
            } else {
                redirect.addFlashAttribute("message", "Test saved: SAFE");
            }
        }
        return "redirect:/water/sources/" + id;
    }

    // ---------- Issues ----------

    @GetMapping("/issues")
    public String issues(@RequestParam(required = false) WorkStatus status, Model model) {
        model.addAttribute("issues", issueService.search(status));
        model.addAttribute("selectedStatus", status);
        return "issues/list";
    }

    @GetMapping("/issues/{id}")
    public String issueDetail(@PathVariable Long id, Model model) {
        model.addAttribute("issue", issueService.findById(id));
        model.addAttribute("actions", issueService.findActions(id));
        return "issues/detail";
    }

    @PostMapping("/issues/{id}/actions")
    public String addAction(@PathVariable Long id,
                            @Valid @ModelAttribute CorrectiveActionRequest req,
                            BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            redirect.addFlashAttribute("error", "Action not saved: type, assignee and date are required.");
        } else {
            issueService.addAction(id, req);
            redirect.addFlashAttribute("message", "Corrective action added");
        }
        return "redirect:/water/issues/" + id;
    }

    private WaterSource findSource(Long id) {
        return sourceService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Water source not found"));
    }
}
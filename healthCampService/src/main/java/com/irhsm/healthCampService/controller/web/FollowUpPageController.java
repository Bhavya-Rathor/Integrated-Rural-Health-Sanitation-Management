package com.irhsm.healthCampService.controller.web;

import com.irhsm.healthCampService.entity.FollowUpStatus;
import com.irhsm.healthCampService.service.FollowUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// GET /camps/followups -> every follow-up, filter by status, close it with an outcome
@Controller
@RequestMapping("/camps/followups")
@RequiredArgsConstructor
public class FollowUpPageController {

    private final FollowUpService followUpService;

    @GetMapping
    public String list(@RequestParam(required = false) FollowUpStatus status, Model model) {
        model.addAttribute("followUps", followUpService.search(status));
        model.addAttribute("statuses", FollowUpStatus.values());
        model.addAttribute("selectedStatus", status);
        return "followups/list";
    }

    @PostMapping("/{id}/status")
    public String update(@PathVariable Long id, @RequestParam String status,
                         @RequestParam(required = false) String outcome,
                         RedirectAttributes redirect) {
        try {
            followUpService.updateStatus(id, status, outcome);
            redirect.addFlashAttribute("message", "Follow-up #" + id + " marked " + status);
        } catch (ResponseStatusException e) {
            redirect.addFlashAttribute("error", e.getReason());
        }
        return "redirect:/camps/followups";
    }
}

package com.irhsn.dashboard.controller;

import com.irhsn.dashboard.dto.PendingRow;
import com.irhsn.dashboard.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// The dashboard page. "/" so that localhost:8080 opens it through the gateway.
@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // GET / or /dashboard  (optional ?village=...&service=...)
    @GetMapping({"/", "/dashboard"})
    public String dashboard(@RequestParam(required = false) String village,
                            @RequestParam(required = false) String service,
                            Model model) {
        List<PendingRow> all = dashboardService.getAllPending();
        List<PendingRow> shown = dashboardService.filter(all, village, service);

        model.addAttribute("tiles", dashboardService.getTiles());
        model.addAttribute("pending", shown);
        model.addAttribute("totalPending", all.size());
        model.addAttribute("highPending",
                all.stream().filter(r -> "HIGH".equalsIgnoreCase(r.item().priority())).count());
        model.addAttribute("villages", dashboardService.villages(all));
        model.addAttribute("services", dashboardService.services());
        model.addAttribute("selectedVillage", village);
        model.addAttribute("selectedService", service);
        return "dashboard/index";   // -> templates/dashboard/index.html
    }

    // POST /dashboard/action  (Approve / Reject buttons)
    @PostMapping("/dashboard/action")
    public String action(@RequestParam String serviceKey,
                         @RequestParam Long id,
                         @RequestParam String decision,          // "approve" or "reject"
                         @RequestParam(required = false) String remarks,
                         RedirectAttributes redirect) {
        boolean approve = "approve".equals(decision);
        String error = dashboardService.decide(serviceKey, id, approve, remarks);

        if (error == null) {
            redirect.addFlashAttribute("message",
                    (approve ? "Approved" : "Rejected") + " item #" + id + " (" + serviceKey + ")");
        } else {
            redirect.addFlashAttribute("error", error);
        }
        return "redirect:/dashboard";   // reload with fresh data
    }
}
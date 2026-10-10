package com.irhsm.sanitationservice.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SanitationHomeController {

    @GetMapping({"/sanitation", "/sanitation/"})
    public String home() {
        return "redirect:/sanitation/facilities";
    }
}
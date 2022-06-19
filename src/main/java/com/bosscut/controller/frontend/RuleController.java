package com.bosscut.controller.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rules")
public class RuleController {

    @GetMapping
    public String home() {
        return "frontend/rule/index";
    }
}

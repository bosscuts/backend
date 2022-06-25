package com.bosscut.controller;

import com.bosscut.entity.Rule;
import com.bosscut.service.RuleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/rules")
public class RuleController {
    private final RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @GetMapping
    public String home(Model model) {
        List<Rule> rules = ruleService.findAll();
        model.addAttribute("rules", rules);
        return "frontend/rule/index";
    }
}

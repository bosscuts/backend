package com.bosscut.controller;

import com.bosscut.entity.User;
import com.bosscut.enums.UserLevel;
import com.bosscut.service.InvoiceService;
import com.bosscut.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/statistic")
public class StatisticController {

    private final UserService userService;
    private final InvoiceService invoiceService;

    public StatisticController(UserService userService, InvoiceService invoiceService) {
        this.userService = userService;
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public String statistic(Model model) {
        List<User> users = userService.getUserByLevel(UserLevel.ASSISTANT.getName());
        model.addAttribute("assistants", users);
        model.addAttribute("turnover", invoiceService.getTurnOver("", ""));
        return "frontend/statistic/index";
    }
}

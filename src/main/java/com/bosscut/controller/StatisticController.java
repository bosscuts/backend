package com.bosscut.controller;

import com.bosscut.entity.User;
import com.bosscut.enums.UserLevel;
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

    public StatisticController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String statistic(Model model) {
        List<User> users = userService.getUserByLevel(UserLevel.ASSISTANT.getName());
        model.addAttribute("assistants", users);
        return "frontend/statistic/index";
    }
}

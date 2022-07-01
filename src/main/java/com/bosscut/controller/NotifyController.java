package com.bosscut.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notification")
public class NotifyController {

    @GetMapping
    @Transactional
    public String notification() {

        return "frontend/notification/index";
    }
}

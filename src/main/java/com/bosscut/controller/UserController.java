package com.bosscut.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    @GetMapping("/cash-advance")
    public String cashAdvance() {
        return "frontend/user/cash";
    }

    @GetMapping("/holiday")
    public String holiday() {
        return "frontend/user/holiday";
    }


    @GetMapping("/account-info")
    public String accountInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "frontend/user/profile";
    }
}
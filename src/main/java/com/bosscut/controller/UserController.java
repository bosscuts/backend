package com.bosscut.controller;

import com.bosscut.entity.User;
import com.bosscut.enums.UserLevel;
import com.bosscut.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/cash-advance")
    public String cashAdvance(Model model) {
        List<User> users = userService.getAll();
        model.addAttribute("staffs", users);
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

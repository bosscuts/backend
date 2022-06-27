package com.bosscut.controller;

import com.bosscut.dto.CashRequestDTO;
import com.bosscut.entity.User;
import com.bosscut.service.SalaryCashService;
import com.bosscut.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final SalaryCashService salaryCashService;

    public UserController(UserService userService, SalaryCashService salaryCashService) {
        this.userService = userService;
        this.salaryCashService = salaryCashService;
    }

    @GetMapping("/cash-advance")
    public String cashAdvance(Model model) {
        List<User> users = userService.getAll();
        model.addAttribute("staffs", users);
        return "frontend/user/cash";
    }

    @PostMapping("/cash-advance")
    public ResponseEntity<?> cashAdvanceProceed(@RequestBody @Valid CashRequestDTO requestDTO) {
        return ResponseEntity.ok(salaryCashService.create(requestDTO));
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

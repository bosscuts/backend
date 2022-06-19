package com.bosscut.controller.backend;

import com.bosscut.dto.InvoiceRequestDTO;
import com.bosscut.entity.User;
import com.bosscut.enums.UserLevel;
import com.bosscut.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/invoice")
public class InvoiceController {

    private final UserService userService;

    public InvoiceController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String invoices(Model model) {
        List<User> users = userService.getUserByLevel(UserLevel.ASSISTANT.getName());
        model.addAttribute("assistants", users);
        return "backend/invoice/index";
    }

    @PostMapping
    public ResponseEntity<?> createInvoice(@RequestBody @Valid InvoiceRequestDTO requestDTO) {
        List<User> users = userService.getUserByLevel(UserLevel.ASSISTANT.getName());
        return ResponseEntity.ok().build();
    }
}
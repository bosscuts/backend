package com.bosscut.controller.backend;

import com.bosscut.dto.InvoiceRequestDTO;
import com.bosscut.entity.User;
import com.bosscut.enums.UserLevel;
import com.bosscut.service.InvoiceService;
import com.bosscut.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/backend")
public class InvoiceController {

    private final UserService userService;
    private final InvoiceService invoiceService;

    public InvoiceController(UserService userService, InvoiceService invoiceService) {
        this.userService = userService;
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoice")
    public String invoices(Model model) {
        List<User> users = userService.getUserByLevel(UserLevel.ASSISTANT.getName());
        model.addAttribute("assistants", users);
        return "backend/invoice/index";
    }

    @GetMapping("/invoice/{staffId}")
    public ResponseEntity<?> getInvoiceByStaffId(@PathVariable String staffId) {
        return ResponseEntity.ok(invoiceService.getInvoiceByStaffId(staffId));
    }

    @PostMapping("/invoice")
    public ResponseEntity<?> createInvoice(@RequestBody @Valid InvoiceRequestDTO requestDTO) {
        invoiceService.createInvoice(requestDTO);
        return ResponseEntity.ok().build();
    }
}
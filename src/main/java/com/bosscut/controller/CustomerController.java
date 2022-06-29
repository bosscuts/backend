package com.bosscut.controller;

import com.bosscut.dto.CustomerRequest;
import com.bosscut.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "frontend/customer/register";
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody @Valid CustomerRequest requestDTO) {
        customerService.create(requestDTO);
        return ResponseEntity.ok().build();
    }
}

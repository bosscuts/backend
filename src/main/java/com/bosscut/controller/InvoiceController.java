package com.bosscut.controller;

import com.bosscut.dto.InvoiceExternalRequest;
import com.bosscut.dto.InvoiceInternalRequest;
import com.bosscut.entity.ProductService;
import com.bosscut.entity.User;
import com.bosscut.enums.ProductServiceType;
import com.bosscut.enums.UserLevel;
import com.bosscut.model.UserInvoiceDetail;
import com.bosscut.service.InvoiceService;
import com.bosscut.service.ProductServiceService;
import com.bosscut.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    private final UserService userService;
    private final InvoiceService invoiceService;

    private final ProductServiceService productServiceService;

    public InvoiceController(UserService userService, InvoiceService invoiceService, ProductServiceService productServiceService) {
        this.userService = userService;
        this.invoiceService = invoiceService;
        this.productServiceService = productServiceService;
    }

    @GetMapping
    @Transactional
    public String invoices(Model model) {
        List<User> users = userService.getUserByLevel(UserLevel.ASSISTANT.getName());

        users.forEach(user -> {
            user.getAuthorities().forEach(authority -> {
                if (authority.getName().equals("ROLE_ASSISTANT")) {
                    user.setAuthorityName(UserLevel.ASSISTANT.getName());
                }
                if (authority.getName().equals("ROLE_MANAGER")) {
                    user.setAuthorityName(UserLevel.MANAGER.getName());
                }
            });
        });

        List<ProductService> productServices = productServiceService.findAll();

        List<ProductService> products = productServices.stream()
                .filter(p -> p.getType().equals(ProductServiceType.PRODUCT.getName()))
                .collect(Collectors.toList());

        List<ProductService> services = productServices.stream()
                .filter(p -> p.getType().equals(ProductServiceType.SERVICE.getName()))
                .collect(Collectors.toList());

        model.addAttribute("assistants", users);
        model.addAttribute("products", products);
        model.addAttribute("services", services);
        return "frontend/invoice/index";
    }

    @GetMapping("/{staffIds}")
    public ResponseEntity<?> getInvoiceByStaffId(@PathVariable String staffIds, @RequestParam(required = false, defaultValue = "false") boolean isMonth) {
        List<UserInvoiceDetail> invoiceDetails;
        if (isMonth)
            invoiceDetails = invoiceService.getInvoiceByStaffIdInMonth(staffIds);
        else
            invoiceDetails = invoiceService.getInvoiceByStaffId(staffIds);
        return ResponseEntity.ok(invoiceDetails);
    }

    @PostMapping
    public ResponseEntity<?> createInvoice(@RequestBody @Valid InvoiceExternalRequest requestDTO) {
        invoiceService.createInvoiceService(requestDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/internal")
    public String invoiceInternal(Model model) {
        List<User> users = userService.getUserByLevel(UserLevel.ASSISTANT.getName());
        model.addAttribute("assistants", users);
        return "frontend/invoice/internal";
    }

    @PostMapping("/internal")
    public ResponseEntity<?> createInvoiceInternal(@RequestBody @Valid InvoiceInternalRequest requestDTO) {
        invoiceService.createInvoiceInternal(requestDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/preview")
    public ResponseEntity<?> previewCreateInvoice(@RequestBody @Valid InvoiceExternalRequest requestDTO) {
        return ResponseEntity.ok(invoiceService.previewInvoice(requestDTO));
    }
}
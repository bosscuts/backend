package com.bosscut.controller;

import com.bosscut.dto.InvoiceExternalRequest;
import com.bosscut.dto.InvoiceInternalRequest;
import com.bosscut.entity.User;
import com.bosscut.enums.UserLevel;
import com.bosscut.model.UserInvoiceDetail;
import com.bosscut.service.InvoiceService;
import com.bosscut.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    private final UserService userService;
    private final InvoiceService invoiceService;

    public InvoiceController(UserService userService, InvoiceService invoiceService) {
        this.userService = userService;
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public String invoices(Model model) {
        List<User> users = userService.getUserByLevel(UserLevel.ASSISTANT.getName());
        model.addAttribute("assistants", users);
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
        invoiceService.createInvoice(requestDTO);
        return ResponseEntity.ok().build();
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
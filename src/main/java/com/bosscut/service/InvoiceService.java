package com.bosscut.service;

import com.bosscut.dto.InvoicePreviewDTO;
import com.bosscut.dto.InvoiceRequestDTO;
import com.bosscut.model.UserInvoiceDetail;

import java.util.List;

public interface InvoiceService {

    void createInvoice(InvoiceRequestDTO requestDTO);
    InvoicePreviewDTO previewInvoice(InvoiceRequestDTO requestDTO);
    List<UserInvoiceDetail> getInvoiceByStaffId(String staffId);
    List<UserInvoiceDetail> getInvoiceByStaffIdInMonth(String staffId);



}

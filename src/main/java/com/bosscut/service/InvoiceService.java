package com.bosscut.service;

import com.bosscut.dto.InvoiceRequestDTO;
import com.bosscut.entity.Invoice;
import com.bosscut.entity.InvoiceDetail;
import com.bosscut.model.UserInvoiceDetail;

import java.util.List;

public interface InvoiceService {

    void createInvoice(InvoiceRequestDTO requestDTO);
    List<UserInvoiceDetail> getInvoiceByStaffId(String staffId);

}

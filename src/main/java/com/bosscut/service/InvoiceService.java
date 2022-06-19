package com.bosscut.service;

import com.bosscut.dto.InvoiceRequestDTO;

public interface InvoiceService {

    void createInvoice(InvoiceRequestDTO requestDTO);
}

package com.bosscut.service;

import com.bosscut.dto.InvoiceInternalRequest;
import com.bosscut.dto.InvoicePreview;
import com.bosscut.dto.InvoiceExternalRequest;
import com.bosscut.model.TurnOver;
import com.bosscut.model.UserInvoiceDetail;

import java.util.List;

public interface InvoiceService {

    void createInvoiceService(InvoiceExternalRequest requestDTO);
    void createInvoiceInternal(InvoiceInternalRequest requestDTO);
    InvoicePreview previewInvoice(InvoiceExternalRequest requestDTO);
    List<UserInvoiceDetail> getInvoiceByStaffId(String staffId);
    List<UserInvoiceDetail> getInvoiceByStaffIdInMonth(String staffId);
    TurnOver getTurnOver(String startOfDay, String endOfDay);
}

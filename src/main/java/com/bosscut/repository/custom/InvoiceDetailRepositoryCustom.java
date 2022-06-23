package com.bosscut.repository.custom;

import com.bosscut.model.UserInvoiceDetail;

import java.util.List;

public interface InvoiceDetailRepositoryCustom {

    List<UserInvoiceDetail> findByStaffIds(List<Long> userIds);
}

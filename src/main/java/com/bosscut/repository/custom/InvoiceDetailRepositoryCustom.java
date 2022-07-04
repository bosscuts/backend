package com.bosscut.repository.custom;

import com.bosscut.model.UserIncome;
import com.bosscut.model.UserInvoiceDetail;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceDetailRepositoryCustom {

    List<UserInvoiceDetail> findByStaffIds(List<Long> userIds, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<UserIncome> findByStaffIdsAndRequestType(List<Long> staffIds, String requestType, LocalDateTime startOfMonth, LocalDateTime endOfMonth);
}

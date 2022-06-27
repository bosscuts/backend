package com.bosscut.service;

import com.bosscut.dto.CashRequestDTO;
import com.bosscut.entity.SalaryCash;

public interface SalaryCashService {

    SalaryCash create(CashRequestDTO dto);
}

package com.bosscut.service;

import com.bosscut.dto.CashRequest;
import com.bosscut.entity.SalaryCash;

public interface SalaryCashService {

    SalaryCash create(CashRequest dto);
}

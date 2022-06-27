package com.bosscut.service.impl;

import com.bosscut.dto.CashRequestDTO;
import com.bosscut.entity.SalaryCash;
import com.bosscut.repository.SalaryCashRepository;
import com.bosscut.service.SalaryCashService;
import org.springframework.stereotype.Service;

@Service
public class SalaryCashServiceImpl implements SalaryCashService {

    private final SalaryCashRepository salaryCashRepository;

    public SalaryCashServiceImpl(SalaryCashRepository salaryCashRepository) {
        this.salaryCashRepository = salaryCashRepository;
    }

    @Override
    public SalaryCash create(CashRequestDTO dto) {

        SalaryCash salaryCash = new SalaryCash();
        salaryCash.setStaffId(dto.getStaffId());
        salaryCash.setAmount(dto.getCashAmount());
        salaryCash.setCashDescription(dto.getCashDescription());
        return salaryCashRepository.save(salaryCash);
    }
}

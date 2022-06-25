package com.bosscut.service.impl;

import com.bosscut.entity.Rule;
import com.bosscut.repository.RuleRepository;
import com.bosscut.service.RuleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleServiceImpl implements RuleService {

    private final RuleRepository ruleRepository;

    public RuleServiceImpl(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Override
    public List<Rule> findAll() {
        return ruleRepository.findAll();
    }
}

package com.bosscut.service.impl;

import com.bosscut.entity.User;
import com.bosscut.enums.RequestType;
import com.bosscut.model.UserIncome;
import com.bosscut.repository.InvoiceDetailRepository;
import com.bosscut.repository.SalaryRepository;
import com.bosscut.repository.UserRepository;
import com.bosscut.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SalaryRepository salaryRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;

    public UserServiceImpl(UserRepository userRepository, SalaryRepository salaryRepository, InvoiceDetailRepository invoiceDetailRepository) {
        this.userRepository = userRepository;
        this.salaryRepository = salaryRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
    }

    public List<User> getUserByLevel(String level) {
        return userRepository.findOneByLevel(level).orElse(Collections.emptyList());
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findOneByLogin(username).orElse(null);
    }

    public List<User> getAll() {
        return userRepository.findAllUser().orElse(Collections.emptyList());
    }

    @Override
    public List<UserIncome> getIncomeByUserIds(String userIds) {
        String[] staffIdArr = userIds.split(",");
        LocalDate initial = LocalDate.now();
        LocalDateTime startOfMonth = initial.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = initial.withDayOfMonth(initial.getMonth().length(initial.isLeapYear()))
                .atStartOfDay().withHour(23).withMinute(59).withSecond(59).withNano(999);

        List<Long> idStaffs = Stream.of(staffIdArr).map(Long::valueOf).collect(Collectors.toList());
        return invoiceDetailRepository.findByStaffIdsAndRequestType(
                idStaffs, RequestType.CASH.getName(), startOfMonth, endOfMonth
        );
    }
}

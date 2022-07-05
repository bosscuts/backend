package com.bosscut.service;

import com.bosscut.entity.User;
import com.bosscut.model.UserIncome;

import java.util.List;

public interface UserService {
    List<User> getUserByLevel(String level);
    User getUserByUsername(String level);

    List<User> getAll();

    List<UserIncome> getIncomeByUserIds(String userIds);
}

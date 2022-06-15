package com.bosscut.service;

import com.bosscut.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    List<User> getUserByLevel(String level);
}

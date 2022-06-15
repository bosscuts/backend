package com.bosscut.service.impl;

import com.bosscut.entity.User;
import com.bosscut.repository.UserRepository;
import com.bosscut.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUserByLevel(String level) {
        return userRepository.findOneByLevel(level).orElse(Collections.emptyList());
    }
}

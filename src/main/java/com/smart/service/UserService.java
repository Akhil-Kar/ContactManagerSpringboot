package com.smart.service;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        user.setRole("ROLE_USER");
        user.setEnable(true);

        String newPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(newPassword);

        User result = userRepository.save(user);

        return result;
    }

    public User updateUser(User user) {
        User result = userRepository.save(user);
        return result;
    }

    public User getUser(String username) {
        User user = userRepository.findByEmail(username);
        return user;
    }
}

package com.stockcommunity.demo.service;

import com.stockcommunity.demo.entity.User;
import com.stockcommunity.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("用戶名已被使用");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email 已被註冊");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);

        return userRepository.save(user);
    }

    public User login(String username,
                      String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用戶名或密碼錯誤"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("用戶名或密碼錯誤");
        }
        return user;
    }
}

package com.example.HomeWorkDev18.user;

import com.example.HomeWorkDev18.security.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public SecurityConfig.User findByLogin(String login) {
        Optional<SecurityConfig.User> user = repository.findByLogin(login);

        return user.orElse(null);
    }

    public void saveUser(SecurityConfig.User user) {
        repository.save(user);
    }
}
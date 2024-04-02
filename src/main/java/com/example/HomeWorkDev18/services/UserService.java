package com.example.HomeWorkDev18.services;

import com.example.HomeWorkDev18.model.User;
import com.example.HomeWorkDev18.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User findByUsername(String email) {
        Optional<User> user = repository.findByLogin(email);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    public void saveUser(User user) {
        repository.save(user);
    }
}
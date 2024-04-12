package com.example.HomeWorkDev18.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User findByLogin(String login) {
        Optional<User> user = repository.findByLogin(login);

        return user.orElse(null);
    }

    public void saveUser(User user) {
        repository.save(user);
    }
}
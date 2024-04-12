package com.example.HomeWorkDev18.auth;

import com.example.HomeWorkDev18.auth.response.LoginResponse;
import com.example.HomeWorkDev18.auth.response.RegistrationResponse;
import com.example.HomeWorkDev18.auth.request.LoginRequest;
import com.example.HomeWorkDev18.auth.request.RegistrationRequest;
import com.example.HomeWorkDev18.security.JwtUtil;
import com.example.HomeWorkDev18.user.User;
import com.example.HomeWorkDev18.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final int MAX_USER_LOGIN_LENGTH = 100;
    private static final int MAX_PASSWORD_LENGTH = 255;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public RegistrationResponse register(RegistrationRequest request) {
        User existingUser = userService.findByLogin(request.getLogin());

        if (Objects.nonNull(existingUser)) {
            return RegistrationResponse.failed(RegistrationResponse.Error.userAlreadyExists);
        }

        Optional<RegistrationResponse.Error> validationError = validateRegistrationFields(request);

        if (validationError.isPresent()) {
            return RegistrationResponse.failed(validationError.get());
        }

        userService.saveUser(User.builder()
                .login(request.getLogin())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .build());

        return RegistrationResponse.success();
    }

    public LoginResponse login(LoginRequest request) {
        Optional<LoginResponse.Error> validationError = validateLoginFields(request);

        if (validationError.isPresent()) {
            return LoginResponse.failed(validationError.get());
        }

        User user = userService.findByLogin(request.getLogin());

        if (Objects.isNull(user)) {
            return LoginResponse.failed(LoginResponse.Error.invalidLogin);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return LoginResponse.failed(LoginResponse.Error.invalidPassword);
        }

        String authToken = jwtUtil.generateToken(request.getLogin());

        return LoginResponse.success(authToken);
    }

    private Optional<RegistrationResponse.Error> validateRegistrationFields(RegistrationRequest request) {
        if (Objects.isNull(request.getLogin()) || request.getLogin().length() > MAX_USER_LOGIN_LENGTH) {
            return Optional.of(RegistrationResponse.Error.invalidEmail);
        }

        if (Objects.isNull(request.getPassword()) || request.getPassword().length() > MAX_PASSWORD_LENGTH) {
            return Optional.of(RegistrationResponse.Error.invalidPassword);
        }

        if (Objects.isNull(request.getName()) || request.getName().length() > MAX_USER_LOGIN_LENGTH) {
            return Optional.of(RegistrationResponse.Error.invalidName);
        }

        return Optional.empty();
    }

    private Optional<LoginResponse.Error> validateLoginFields(LoginRequest request) {
        if (Objects.isNull(request.getLogin()) || request.getLogin().length() > MAX_USER_LOGIN_LENGTH) {
            return Optional.of(LoginResponse.Error.invalidLogin);
        }

        if (Objects.isNull(request.getPassword()) || request.getPassword().length() > MAX_PASSWORD_LENGTH) {
            return Optional.of(LoginResponse.Error.invalidPassword);
        }

        return Optional.empty();
    }
}
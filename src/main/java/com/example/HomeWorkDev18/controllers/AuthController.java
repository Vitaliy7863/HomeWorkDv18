package com.example.HomeWorkDev18.controllers;

import com.example.HomeWorkDev18.request.LoginRequest;
import com.example.HomeWorkDev18.request.RegistrationRequest;
import com.example.HomeWorkDev18.response.LoginResponse;
import com.example.HomeWorkDev18.response.RegistrationResponse;
import com.example.HomeWorkDev18.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public RegistrationResponse register( @RequestBody RegistrationRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse register(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
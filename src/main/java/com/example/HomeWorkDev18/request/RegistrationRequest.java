package com.example.HomeWorkDev18.request;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String login;
    private String password;
    private String name;
}
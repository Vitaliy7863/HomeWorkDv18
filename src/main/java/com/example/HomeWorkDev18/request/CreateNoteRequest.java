package com.example.HomeWorkDev18.request;

import lombok.Data;

@Data
public class CreateNoteRequest {
    private String title;
    private String content;
}
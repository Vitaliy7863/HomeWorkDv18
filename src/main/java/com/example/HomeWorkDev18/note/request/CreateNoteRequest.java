package com.example.HomeWorkDev18.note.request;

import lombok.Data;

@Data
public class CreateNoteRequest {
    private String title;
    private String content;
}
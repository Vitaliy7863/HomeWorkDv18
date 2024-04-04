package com.example.HomeWorkDev18.controllers;

import com.example.HomeWorkDev18.request.CreateNoteRequest;
import com.example.HomeWorkDev18.request.UpdateNoteRequest;
import com.example.HomeWorkDev18.response.CreateNoteResponse;
import com.example.HomeWorkDev18.response.DeleteNoteResponse;
import com.example.HomeWorkDev18.response.GetUserNotesResponse;
import com.example.HomeWorkDev18.response.UpdateNoteResponse;
import com.example.HomeWorkDev18.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    public CreateNoteResponse create(Principal principal, @RequestBody CreateNoteRequest request) {
        return noteService.create(principal.getName(), request);
    }

    @GetMapping
    public GetUserNotesResponse getUserNotes(Principal principal) {
        return noteService.getUserNotes(principal.getName());
    }

    @PatchMapping
    public UpdateNoteResponse update(Principal principal, @RequestBody UpdateNoteRequest request) {
        return noteService.update(principal.getName(), request);
    }

    @DeleteMapping
    public DeleteNoteResponse delete(Principal principal, @RequestParam(name = "id") long id) {
        return noteService.delete(principal.getName(), id);
    }
}
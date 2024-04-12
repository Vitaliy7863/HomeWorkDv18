package com.example.HomeWorkDev18.note;

import com.example.HomeWorkDev18.note.response.CreateNoteResponse;
import com.example.HomeWorkDev18.note.response.DeleteNoteResponse;
import com.example.HomeWorkDev18.note.response.GetUserNotesResponse;
import com.example.HomeWorkDev18.note.response.UpdateNoteResponse;
import com.example.HomeWorkDev18.note.request.CreateNoteRequest;
import com.example.HomeWorkDev18.note.request.UpdateNoteRequest;
import com.example.HomeWorkDev18.user.UserService;
import com.example.HomeWorkDev18.security.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {
    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_CONTENT_LENGTH = 1000;

    private final UserService userService;
    private final NoteRepository repository;

    public CreateNoteResponse create(String login, CreateNoteRequest request) {
        Optional<CreateNoteResponse.Error> validationError = validateCreateFields(request);

        if (validationError.isPresent()) {
            return CreateNoteResponse.failed(validationError.get());
        }

        SecurityConfig.User user = userService.findByLogin(login);

        Note createdNote = repository.save(Note.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .build());

        return CreateNoteResponse.success(createdNote.getId());
    }

    public GetUserNotesResponse getUserNotes(String login) {
        List<Note> userNotes = repository.getUserNotes(login);
        return GetUserNotesResponse.success(userNotes);
    }

    public UpdateNoteResponse update(String login, UpdateNoteRequest request) {
        Optional<Note> optionalNote = repository.findById(request.getId());

        if (optionalNote.isEmpty()) {
            return UpdateNoteResponse.failed(UpdateNoteResponse.Error.invalidNoteId);
        }

        Note note = optionalNote.get();

        boolean isNotUserNote = isNotUserNote(login, note);

        if (isNotUserNote) {
            return UpdateNoteResponse.failed(UpdateNoteResponse.Error.insufficientPrivileges);
        }

        Optional<UpdateNoteResponse.Error> validationError = validateUpdateFields(request);

        if (validationError.isPresent()) {
            return UpdateNoteResponse.failed(validationError.get());
        }

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());

        repository.save(note);

        return UpdateNoteResponse.success(note);
    }

    public DeleteNoteResponse delete(String login, long id) {
        Optional<Note> optionalNote = repository.findById(id);

        if (optionalNote.isEmpty()) {
            return DeleteNoteResponse.failed(DeleteNoteResponse.Error.invalidNoteId);
        }

        Note note = optionalNote.get();

        boolean isNotUserNote = isNotUserNote(login, note);

        if (isNotUserNote) {
            return DeleteNoteResponse.failed(DeleteNoteResponse.Error.insufficientPrivileges);
        }

        repository.delete(note);

        return DeleteNoteResponse.success();
    }

    private Optional<CreateNoteResponse.Error> validateCreateFields(CreateNoteRequest request) {
        if (Objects.isNull(request.getTitle()) || request.getTitle().length() > MAX_TITLE_LENGTH) {
            return Optional.of(CreateNoteResponse.Error.invalidTitle);
        }

        if (Objects.isNull(request.getContent()) || request.getContent().length() > MAX_CONTENT_LENGTH) {
            return Optional.of(CreateNoteResponse.Error.invalidContent);
        }

        return Optional.empty();
    }

    private Optional<UpdateNoteResponse.Error> validateUpdateFields(UpdateNoteRequest request) {
        if (Objects.nonNull(request.getTitle()) && request.getTitle().length() > MAX_TITLE_LENGTH) {
            return Optional.of(UpdateNoteResponse.Error.invalidTitleLength);
        }

        if (Objects.nonNull(request.getContent()) && request.getContent().length() > MAX_CONTENT_LENGTH) {
            return Optional.of(UpdateNoteResponse.Error.invalidContentLength);
        }

        return Optional.empty();
    }

    private boolean isNotUserNote(String login, Note note) {
        return !note.getUser().getLogin().equals(login);
    }
}
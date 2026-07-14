package com.mallya.notesapi.controller;

import com.mallya.notesapi.dto.note.CreateNotesRequestDTO;
import com.mallya.notesapi.dto.note.NotesResponseDTO;
import com.mallya.notesapi.model.Notes;
import com.mallya.notesapi.repository.UserRepository;
import com.mallya.notesapi.service.NotesService;
import com.mallya.notesapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NotesController {

    private final NotesService notesService;
    private final UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addNotes(@Valid @RequestBody CreateNotesRequestDTO requestDTO, @AuthenticationPrincipal UserDetails userDetails) {
        if (userRepository.existsByEmail(userDetails.getUsername())) {
            NotesResponseDTO responseDTO = notesService.addNotes(requestDTO, userDetails.getUsername());
            return ResponseEntity.ok(responseDTO);
        }
        return ResponseEntity.badRequest().body("Invalid login");
    }

    @GetMapping("/allNotes")
    public ResponseEntity<?> getAllNotes(@AuthenticationPrincipal UserDetails userDetails) {
        if (userRepository.existsByEmail(userDetails.getUsername())) {
            List<NotesResponseDTO> list = notesService.getNotes(userDetails.getUsername());
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.badRequest().body("Invalid login");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNoteById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userRepository.existsByEmail(userDetails.getUsername())) {
            NotesResponseDTO note = notesService.getNotById(id, userDetails.getUsername());
            return ResponseEntity.ok(note);
        }
        return ResponseEntity.badRequest().body("Invalid login");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateNote(@PathVariable Long id, @Valid @RequestBody CreateNotesRequestDTO request, @AuthenticationPrincipal UserDetails userDetails) {
        if (userRepository.existsByEmail(userDetails.getUsername())) {
            NotesResponseDTO note = notesService.updateNote(id, request, userDetails.getUsername());
            return ResponseEntity.ok(note);
        }
        return ResponseEntity.badRequest().body("Invalid login");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userRepository.existsByEmail(userDetails.getUsername())) {
            notesService.deleteNote(id,userDetails.getUsername());
            return ResponseEntity.ok("Note Deleted");
        }
        return ResponseEntity.badRequest().body("Invalid login");

    }
}

package com.mallya.notesapi.controller;

import com.mallya.notesapi.dto.note.CreateNotesRequestDTO;
import com.mallya.notesapi.dto.note.NotesResponseDTO;
import com.mallya.notesapi.repository.UserRepository;
import com.mallya.notesapi.service.NotesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
            NotesResponseDTO responseDTO = notesService.addNotes(requestDTO, userDetails.getUsername());
            return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/allNotes")
    public ResponseEntity<?> getAllNotes(@AuthenticationPrincipal UserDetails userDetails) {
            List<NotesResponseDTO> list = notesService.getNotes(userDetails.getUsername());
            return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNoteById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
            NotesResponseDTO note = notesService.getNoteById(id, userDetails.getUsername());
            return ResponseEntity.ok(note);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateNote(@PathVariable Long id, @Valid @RequestBody CreateNotesRequestDTO request, @AuthenticationPrincipal UserDetails userDetails) {
            NotesResponseDTO note = notesService.updateNote(id, request, userDetails.getUsername());
            return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
            notesService.deleteNote(id,userDetails.getUsername());
            return ResponseEntity.ok("Note Deleted");
    }

    @GetMapping("/search")
    public ResponseEntity<List<NotesResponseDTO>> searchNote(@RequestParam String title, @AuthenticationPrincipal UserDetails userDetails){
        List<NotesResponseDTO> notes = notesService.getNoteBySearch(title,userDetails.getUsername());
        return ResponseEntity.ok(notes);
    }
}

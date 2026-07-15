package com.mallya.notesapi.controller;

import com.mallya.notesapi.dto.note.NotesRequestDTO;
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
import java.util.Map;

@Controller
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NotesController {

    private final NotesService notesService;
    private final UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addNotes(@Valid @RequestBody NotesRequestDTO requestDTO, @AuthenticationPrincipal UserDetails userDetails) {
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
    public ResponseEntity<?> updateNote(@PathVariable Long id, @Valid @RequestBody NotesRequestDTO request, @AuthenticationPrincipal UserDetails userDetails) {
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

    @PutMapping("/moveCategory")
    public ResponseEntity<Map<String,String>> moveNoteCategory(@RequestParam Long from, @RequestParam Long to, @AuthenticationPrincipal UserDetails userDetails){
        Map<String, String> response = notesService.moveNoteCategory(from, to, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/archive/{id}")
    public ResponseEntity<Map<String,String>> archiveNote(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        Map<String, String> response = notesService.archiveNote(id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/unArchive/{id}")
    public ResponseEntity<Map<String,String>> unArchiveNote(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        Map<String, String> response = notesService.unArchiveNote(id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("listAllArchivedNotes")
    public ResponseEntity<List<NotesResponseDTO>> getArchivedNotes(@AuthenticationPrincipal UserDetails userDetails){
        List<NotesResponseDTO> list = notesService.getArchivedNotes(userDetails.getUsername());
        return ResponseEntity.ok(list);
    }
}

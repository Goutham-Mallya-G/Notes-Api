package com.mallya.notesapi.controller;

import com.mallya.notesapi.dto.note.NotesRequestDTO;
import com.mallya.notesapi.dto.note.NotesResponseDTO;
import com.mallya.notesapi.repository.UserRepository;
import com.mallya.notesapi.service.NotesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
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
    public ResponseEntity<Page<NotesResponseDTO>> getAllNotes(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
            Page<NotesResponseDTO> list = notesService.getNotes(userDetails.getUsername(), page, size);
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
    public ResponseEntity<Map<String,String>> deleteNote(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
            return ResponseEntity.ok(notesService.deleteNote(id,userDetails.getUsername()));
    }

    @PatchMapping("restore/{id}")
    public ResponseEntity<Map<String,String>> restoreNote(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(notesService.restoreNote(id, userDetails.getUsername()));
    }


    @GetMapping("/searchByTitle")
    public ResponseEntity<Page<NotesResponseDTO>> searchNoteByTitle(@RequestParam String title, @AuthenticationPrincipal UserDetails userDetails,@RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size){
        Page<NotesResponseDTO> notes = notesService.getNoteBySearchByTitle(title,userDetails.getUsername(), page, size);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/searchByContent")
    public ResponseEntity<Page<NotesResponseDTO>> searchNoteByContent(@RequestParam String content, @AuthenticationPrincipal UserDetails userDetails,@RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size){
        Page<NotesResponseDTO> notes = notesService.getNoteBySearchByContent(content,userDetails.getUsername(), page, size);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/searchByQuery")
    public ResponseEntity<Page<NotesResponseDTO>> searchNoteByQuery(@RequestParam String query, @AuthenticationPrincipal UserDetails userDetails,@RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size){
        Page<NotesResponseDTO> notes = notesService.getNoteBySearchByQuery(query,userDetails.getUsername(), page, size);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/searchDeletedByTitle")
    public ResponseEntity<Page<NotesResponseDTO>> searchDeletedNoteByTitle(@RequestParam String title, @AuthenticationPrincipal UserDetails userDetails,@RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size){
        Page<NotesResponseDTO> notes = notesService.getDeletedNoteBySearchByTitle(title,userDetails.getUsername(), page, size);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/searchDeletedByContent")
    public ResponseEntity<Page<NotesResponseDTO>> searchDeletedNoteByContent(@RequestParam String content, @AuthenticationPrincipal UserDetails userDetails, @RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size){
        Page<NotesResponseDTO> notes = notesService.getDeletedNoteBySearchByContent(content,userDetails.getUsername(), page, size);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/searchDeletedByQuery")
    public ResponseEntity<Page<NotesResponseDTO>> searchDeletedNoteByQuery(@RequestParam String query, @AuthenticationPrincipal UserDetails userDetails,@RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size){
        Page<NotesResponseDTO> notes = notesService.getDeletedNoteBySearchByQuery(query,userDetails.getUsername(), page, size);
        return ResponseEntity.ok(notes);
    }

    @DeleteMapping("/deleteTrash/{id}")
    public ResponseEntity<Map<String,String>> deleteNotesInTrash(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(notesService.deleteNoteInTrash(id,userDetails.getUsername()));
    }

    @DeleteMapping("/emptyTrash")
    public ResponseEntity<Map<String,String>> emptyTrash(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(notesService.emptyTrash(userDetails.getUsername()));
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
    public ResponseEntity<Page<NotesResponseDTO>> getArchivedNotes(@AuthenticationPrincipal UserDetails userDetails,@RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size){
        Page<NotesResponseDTO> list = notesService.getArchivedNotes(userDetails.getUsername(), page, size);
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/favorite/{id}")
    public ResponseEntity<Map<String,String>> favoriteNote(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        Map<String, String> response = notesService.favoriteNote(id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/unFavorite/{id}")
    public ResponseEntity<Map<String,String>> unFavoriteNote(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        Map<String, String> response = notesService.unFavoriteNote(id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("listAllFavoriteNotes")
    public ResponseEntity<Page<NotesResponseDTO>> getFavoriteNotes(@AuthenticationPrincipal UserDetails userDetails,@RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size){
        Page<NotesResponseDTO> list = notesService.getFavoriteNotes(userDetails.getUsername(), page, size);
        return ResponseEntity.ok(list);
    }

}

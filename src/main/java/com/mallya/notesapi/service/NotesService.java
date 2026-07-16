package com.mallya.notesapi.service;

import com.mallya.notesapi.dto.note.NotesRequestDTO;
import com.mallya.notesapi.dto.note.NotesResponseDTO;
import com.mallya.notesapi.exception.CategoryException;
import com.mallya.notesapi.exception.NotesException;
import com.mallya.notesapi.model.Category;
import com.mallya.notesapi.model.Notes;
import com.mallya.notesapi.model.Users;
import com.mallya.notesapi.repository.CategoryRepository;
import com.mallya.notesapi.repository.NotesRepository;
import com.mallya.notesapi.repository.UserRepository;
import com.mallya.notesapi.utils.UtilDto;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotesService {

    private final NotesRepository notesRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UtilDto utilDto;

    public NotesResponseDTO addNotes(@Valid NotesRequestDTO requestDTO, String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));

        Notes note = new Notes();
        note.setTitle(requestDTO.getTitle());
        note.setContent(requestDTO.getContent());
        note.setUser(user);

        Category category = categoryRepository.findByUserEmailAndId(email, requestDTO.getCategoryId()).orElseThrow(() -> new CategoryException("No category Found"));
        note.setCategory(category);
        note.setArchived(false);
        LocalDateTime now = LocalDateTime.now();
        note.setCreatedAt(now);
        note.setUpdatedAt(now);
        category.getNotes().add(note);

        categoryRepository.save(category);

        return utilDto.convertNotesToNotsResponseDTO(notesRepository.save(note));
    }

    public Page<NotesResponseDTO> getNotes(String email, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Notes> notesPage =  notesRepository.findByUserEmailAndArchivedFalseAndDeletedFalse(email, pageable);
        return notesPage.map(utilDto::convertNotesToNotsResponseDTO);
    }

    public NotesResponseDTO getNoteById(Long id, String email) {
        return utilDto.convertNotesToNotsResponseDTO(notesRepository
                        .findByUserEmailAndIdAndDeletedFalse(email, id)
                        .orElseThrow(() -> new NotesException("No Notes found")));
    }

    public NotesResponseDTO updateNote(Long id, @Valid NotesRequestDTO request, String email) {
        Notes note = notesRepository.findByUserEmailAndIdAndDeletedFalse(email, id)
                .orElseThrow(() -> new NotesException("No Notes found"));
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setUpdatedAt(LocalDateTime.now());

        notesRepository.save(note);

        return utilDto.convertNotesToNotsResponseDTO(note);
    }

    public Map<String,String > deleteNote(Long id, String email) {
        Notes note = notesRepository.findByUserEmailAndIdAndDeletedFalse(email,id)
                .orElseThrow(() -> new NotesException("No Notes Found"));

        note.setDeleted(true);
        note.setDeletedAt(LocalDateTime.now());

        notesRepository.save(note);

        return Map.of("Message","Note deleted");
    }

    public Map<String, String> restoreNote(Long id, String email){
        Notes note = notesRepository.findByUserEmailAndIdAndDeletedTrue(email,id)
                .orElseThrow(() -> new NotesException("No Notes Found"));

        note.setDeleted(false);
        note.setDeletedAt(null);

        notesRepository.save(note);
        return Map.of("Message", "Note restored successfully");
    }

    public Page<NotesResponseDTO> getNoteBySearchByTitle(String title, String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notes> notesPage = notesRepository.findByUserEmailAndDeletedFalseAndArchivedFalseAndTitleContainingIgnoreCase(email, title, pageable);
        return notesPage.map(utilDto::convertNotesToNotsResponseDTO);
    }

    public Page<NotesResponseDTO> getNoteBySearchByContent(String content, String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notes> notesPage = notesRepository.findByUserEmailAndDeletedFalseAndArchivedFalseAndContentContainingIgnoreCase(email, content, pageable);
        return notesPage.map(utilDto::convertNotesToNotsResponseDTO);
    }

    public Page<NotesResponseDTO> getNoteBySearchByQuery(String query, String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notes> notesPage = notesRepository.searchNotes(email, query, pageable);
        return notesPage.map(utilDto::convertNotesToNotsResponseDTO);
    }

    public Page<NotesResponseDTO> getDeletedNoteBySearchByTitle(String title, String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notes> notesPage = notesRepository.findByUserEmailAndDeletedTrueAndTitleContainingIgnoreCase(email, title, pageable);
        return notesPage.map(utilDto::convertNotesToNotsResponseDTO);
    }

    public Page<NotesResponseDTO> getDeletedNoteBySearchByContent(String content, String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notes> notesPage = notesRepository.findByUserEmailAndDeletedTrueAndContentContainingIgnoreCase(email, content, pageable);
        return notesPage.map(utilDto::convertNotesToNotsResponseDTO);
    }

    public Page<NotesResponseDTO> getDeletedNoteBySearchByQuery(String query, String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notes> notesPage = notesRepository.searchDeletedNotes(email, query, pageable);
        return notesPage.map(utilDto::convertNotesToNotsResponseDTO);
    }

    public Map<String, String> moveNoteCategory(Long id, Long categoryId, String email) {
        Notes note = notesRepository.findByUserEmailAndIdAndDeletedFalse(email,id)
                .orElseThrow(() -> new NotesException("No Notes Found"));

        Category oldCategory = note.getCategory();
        Category newCategory = categoryRepository.findByUserEmailAndId(email,categoryId).orElseThrow(() -> new CategoryException("No category found to move note"));

        if(oldCategory.equals(newCategory)){
            throw new CategoryException("Cant move note to the existing category");
        }

        List<Notes> oldCategoryNotes = oldCategory.getNotes();

        oldCategoryNotes.remove(note);

        oldCategory.setNotes(oldCategoryNotes);
        categoryRepository.save(oldCategory);

        List<Notes> newCategoryNotes = newCategory.getNotes();

        newCategoryNotes.add(note);
        categoryRepository.save(newCategory);

        note.setCategory(newCategory);
        notesRepository.save(note);

        return Map.of("message" , "Note moved");

    }

    public Map<String, String> archiveNote(Long id, String email) {
        Notes note = notesRepository.findByUserEmailAndIdAndDeletedFalse(email,id).orElseThrow(() -> new NotesException("No note found"));
        if(note.isArchived()){
            throw new NotesException("Note is already archived");
        }
        note.setArchived(true);
        notesRepository.save(note);
        return Map.of("Message","Note archived");
    }

    public Map<String, String> unArchiveNote(Long id, String email) {
        Notes note = notesRepository.findByUserEmailAndIdAndArchivedTrue(email,id).orElseThrow(() -> new NotesException("No note found"));
        if(!note.isArchived()){
            throw new NotesException("Note is already active");
        }
        note.setArchived(false);
        notesRepository.save(note);
        return Map.of("Message","Note is active");
    }

    public Page<NotesResponseDTO> getArchivedNotes(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notes> notesPage = notesRepository.findByUserEmailAndArchivedTrueAndDeletedFalse(email,pageable);
        return notesPage.map(utilDto::convertNotesToNotsResponseDTO);
    }

    public Map<String, String> favoriteNote(Long id, String email) {
        Notes note = notesRepository.findByUserEmailAndIdAndDeletedFalse(email,id).orElseThrow(() ->  new NotesException("No notes found"));
        if(note.isFavorite()){
            throw new NotesException("Note is already favorite");
        }
        note.setFavorite(true);
        notesRepository.save(note);
        return Map.of("Message", "Note set to favorite");
    }

    public Map<String, String> unFavoriteNote(Long id, String email) {
        Notes note = notesRepository.findByUserEmailAndIdAndDeletedFalse(email,id).orElseThrow(() ->  new NotesException("No notes found"));
        if(!note.isFavorite()){
            throw new NotesException("Note is not favorite");
        }
        note.setFavorite(false);
        notesRepository.save(note);
        return Map.of("Message", "Note set to unFavorite");
    }

    public Page<NotesResponseDTO> getFavoriteNotes(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notes> notesPage = notesRepository.findByUserEmailAndFavoriteTrueAndDeletedFalse(email,pageable);
        return notesPage.map(utilDto::convertNotesToNotsResponseDTO);
    }

    public Map<String, String> deleteNoteInTrash(Long id, String email) {
        Notes note = notesRepository.findByUserEmailAndIdAndDeletedTrue(email,id).orElseThrow(() -> new NotesException("No note found"));
        notesRepository.delete(note);
        return Map.of("Message","Note deleted successfully");
    }

    public Map<String, String> emptyTrash(String email) {
        List<Notes> notes = notesRepository.findByUserEmailAndDeletedTrue(email);
        notesRepository.deleteAll(notes);
        return Map.of("Message","Trash emptied");
    }
}

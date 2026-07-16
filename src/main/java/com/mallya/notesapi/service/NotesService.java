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

    public List<NotesResponseDTO> getNotes(String email){
        List<NotesResponseDTO> list = new ArrayList<>();
        for(Notes note : notesRepository.findByUserEmailAndArchivedFalseAndDeletedFalse(email)){
            list.add(utilDto.convertNotesToNotsResponseDTO(note));
        }
        return list;
    }

    public NotesResponseDTO getNoteById(Long id, String email) {
        return utilDto.convertNotesToNotsResponseDTO(notesRepository
                        .findByUserEmailAndIdAndDeletedFalse(email, id)
                        .orElseThrow(() -> new NotesException("No Notes found")));
    }

    public NotesResponseDTO updateNote(Long id, @Valid NotesRequestDTO request, String email) {
        Notes note = notesRepository.findByUserEmailAndIdAndDeletedFalse(email, id)
                .orElseThrow(() -> new NotesException("No Notes found"));

        Category category = categoryRepository.findByUserEmailAndId(email, request.getCategoryId()).orElseThrow(() -> new RuntimeException("No category found"));

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

        notesRepository.save(note);

        return Map.of("Message","Note deleted");
    }

    public Map<String, String> restoreNote(Long id, String email){
        Notes note = notesRepository.findByUserEmailAndIdAndDeletedTrue(email,id)
                .orElseThrow(() -> new NotesException("No Notes Found"));

        note.setDeleted(false);

        notesRepository.save(note);
        return Map.of("Message", "Note restored successfully");
    }

    public List<NotesResponseDTO> getNoteBySearchByTitle(String title, String email) {
        List<NotesResponseDTO> list = new ArrayList<>();
        for(Notes note : notesRepository.findByUserEmailAndDeletedFalseAndArchivedFalseAndTitleContainingIgnoreCase(email, title)) {
            list.add(utilDto.convertNotesToNotsResponseDTO(note));
        }
        return list;
    }

    public List<NotesResponseDTO> getNoteBySearchByContent(String content, String email) {
        List<NotesResponseDTO> list = new ArrayList<>();
        for (Notes note : notesRepository.findByUserEmailAndDeletedFalseAndArchivedFalseAndContentContainingIgnoreCase(email, content)) {
            list.add(utilDto.convertNotesToNotsResponseDTO(note));
        }
        return list;
    }

    public List<NotesResponseDTO> getNoteBySearchByQuery(String query, String email) {
        List<NotesResponseDTO> list = new ArrayList<>();
        for (Notes note : notesRepository.searchNotes(email, query)) {
            list.add(utilDto.convertNotesToNotsResponseDTO(note));
        }
        return list;
    }

    public List<NotesResponseDTO> getDeletedNoteBySearchByTitle(String title, String email) {
        List<NotesResponseDTO> list = new ArrayList<>();
        for(Notes note : notesRepository.findByUserEmailAndDeletedTrueAndTitleContainingIgnoreCase(email, title)) {
            list.add(utilDto.convertNotesToNotsResponseDTO(note));
        }
        return list;
    }

    public List<NotesResponseDTO> getDeletedNoteBySearchByContent(String content, String email) {
        List<NotesResponseDTO> list = new ArrayList<>();
        for(Notes note : notesRepository.findByUserEmailAndDeletedTrueAndAndContentContainingIgnoreCase(email, content)) {
            list.add(utilDto.convertNotesToNotsResponseDTO(note));
        }
        return list;
    }

    public List<NotesResponseDTO> getDeletedNoteBySearchByQuery(String query, String email) {
        List<NotesResponseDTO> list = new ArrayList<>();
        for (Notes note : notesRepository.searchDeletedNotes(email, query)) {
            list.add(utilDto.convertNotesToNotsResponseDTO(note));
        }
        return list;
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

    public List<NotesResponseDTO> getArchivedNotes(String email) {
        List<NotesResponseDTO> list = new ArrayList<>();
        for(Notes note : notesRepository.findByUserEmailAndArchivedTrueAndDeletedFalse(email)){
            list.add(utilDto.convertNotesToNotsResponseDTO(note));
        }
        return list;
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

    public List<NotesResponseDTO> getFavoriteNotes(String email) {
        List<NotesResponseDTO> list = new ArrayList<>();
        for(Notes note : notesRepository.findByUserEmailAndFavoriteTrueAndDeletedFalse(email)){
            list.add(utilDto.convertNotesToNotsResponseDTO(note));
        }
        return list;
    }

    public Map<String, String> deleteNoteInTrash(Long id, String email) {
        Notes note = notesRepository.findByUserEmailAndIdAndDeletedTrue(email,id).orElseThrow(() -> new NotesException("No note found"));
        notesRepository.delete(note);
        return Map.of("Message","Note deleted successfully");
    }
}

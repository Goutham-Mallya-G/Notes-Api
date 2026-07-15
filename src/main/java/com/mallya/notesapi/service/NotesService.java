package com.mallya.notesapi.service;

import com.mallya.notesapi.dto.note.NotesRequestDTO;
import com.mallya.notesapi.dto.note.NotesResponseDTO;
import com.mallya.notesapi.exception.NoteNotFoundException;
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

        Category category = categoryRepository.findByUserEmailAndId(email, requestDTO.getCategoryId()).orElseThrow(() -> new RuntimeException("No category Found"));
        note.setCategory(category);
        List<Notes> list = category.getNotes();
        list.add(note);
        category.setNotes(list);

        categoryRepository.save(category);

        return utilDto.convertNotesToNotsResponseDTO(notesRepository.save(note));
    }

    public List<NotesResponseDTO> getNotes(String email){
        List<NotesResponseDTO> list = new ArrayList<>();
        for(Notes note : notesRepository.findByUserEmail(email)){
            list.add(utilDto.convertNotesToNotsResponseDTO(note));
        }
        return list;
    }

    public NotesResponseDTO getNoteById(Long id, String email) {
        return utilDto.convertNotesToNotsResponseDTO(notesRepository
                        .findByUserEmailAndId(email, id)
                        .orElseThrow(() -> new NoteNotFoundException("No Notes found")));
    }

    public NotesResponseDTO updateNote(Long id, @Valid NotesRequestDTO request, String email) {
        Notes note = notesRepository.findByUserEmailAndId(email, id)
                .orElseThrow(() -> new NoteNotFoundException("No Notes found"));

        Category category = categoryRepository.findByUserEmailAndId(email, request.getCategoryId()).orElseThrow(() -> new RuntimeException("No category found"));

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setCategory(category);

        notesRepository.save(note);

        return utilDto.convertNotesToNotsResponseDTO(note);
    }

    public void deleteNote(Long id, String email) {
        Notes note = notesRepository.findByUserEmailAndId(email,id)
                .orElseThrow(() -> new NoteNotFoundException("No Notes Found"));

        Category category = note.getCategory();
        List<Notes> list = category.getNotes();

        for(Notes notes : list){
            if(notes.equals(note)){
                list.remove(note);
                break;
            }
        }

        category.setNotes(list);
        categoryRepository.save(category);

        notesRepository.delete(note);
    }

    public List<NotesResponseDTO> getNoteBySearch(String title, String email) {
        List<NotesResponseDTO> list = new ArrayList<>();
        for(Notes note : notesRepository.findByUserEmailAndTitleContainingIgnoreCase(email, title)) {
            list.add(utilDto.convertNotesToNotsResponseDTO(note));
        }
        return list;
    }


    public Map<String, String> moveNoteCategory(Long from, Long to, String email) {
        Notes note = notesRepository.findByUserEmailAndId(email,from)
                .orElseThrow(() -> new NoteNotFoundException("No Notes Found"));

        Category oldCategory = categoryRepository.findByUserEmailAndId(email,from).orElseThrow(() -> new RuntimeException("No category found to move note"));
        Category newCategory = categoryRepository.findByUserEmailAndId(email,to).orElseThrow(() -> new RuntimeException("No category found to move note"));

        List<Notes> oldCategoryNotes = oldCategory.getNotes();

        for(Notes notes : oldCategoryNotes){
            if(notes.equals(note)){
                oldCategoryNotes.remove(note);
                break;
            }
        }

        oldCategory.setNotes(oldCategoryNotes);
        categoryRepository.save(oldCategory);

        List<Notes> newCategoryNotes = newCategory.getNotes();

        newCategoryNotes.add(note);
        categoryRepository.save(newCategory);

        note.setCategory(newCategory);

        return Map.of("message" , "Note moved");

    }
}

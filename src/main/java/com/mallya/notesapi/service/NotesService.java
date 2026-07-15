package com.mallya.notesapi.service;

import com.mallya.notesapi.dto.note.CreateNotesRequestDTO;
import com.mallya.notesapi.dto.note.NotesResponseDTO;
import com.mallya.notesapi.exception.NoteNotFoundException;
import com.mallya.notesapi.model.Notes;
import com.mallya.notesapi.model.Users;
import com.mallya.notesapi.repository.NotesRepository;
import com.mallya.notesapi.repository.UserRepository;
import com.mallya.notesapi.utils.UtilDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotesService {

    private final NotesRepository notesRepository;
    private final UserRepository userRepository;
    private final UtilDto utilDto;

    public NotesResponseDTO addNotes(@Valid CreateNotesRequestDTO requestDTO, String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));
        Notes note = new Notes();
        note.setTitle(requestDTO.getTitle());
        note.setContent(requestDTO.getContent());
        note.setUser(user);

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

    public NotesResponseDTO updateNote(Long id, @Valid CreateNotesRequestDTO request, String email) {
        Notes note = notesRepository.findByUserEmailAndId(email, id)
                .orElseThrow(() -> new NoteNotFoundException("No Notes found"));

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());

        notesRepository.save(note);

        return utilDto.convertNotesToNotsResponseDTO(note);
    }

    public void deleteNote(Long id, String email) {
        Notes note = notesRepository.findByUserEmailAndId(email,id)
                .orElseThrow(() -> new NoteNotFoundException("No Notes Found"));

        notesRepository.delete(note);
    }
}

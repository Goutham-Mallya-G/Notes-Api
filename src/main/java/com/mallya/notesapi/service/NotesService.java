package com.mallya.notesapi.service;

import com.mallya.notesapi.dto.note.CreateNotesRequestDTO;
import com.mallya.notesapi.dto.note.NotesResponseDTO;
import com.mallya.notesapi.model.Notes;
import com.mallya.notesapi.model.Users;
import com.mallya.notesapi.repository.NotesRepository;
import com.mallya.notesapi.repository.UserRepository;
import com.mallya.notesapi.utils.UtilDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Path;
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
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));
        List<NotesResponseDTO> list = new ArrayList<>();
        for(Notes note : notesRepository.findByUserEmail(email)){
            list.add(utilDto.convertNotesToNotsResponseDTO(note));
        }
        return list;
    }

    public NotesResponseDTO getNotById(Long id, String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));

        return utilDto.convertNotesToNotsResponseDTO(notesRepository
                        .findByUserEmailAndId(email, id)
                        .orElseThrow(() -> new RuntimeException("No Notes found")));
    }

    public NotesResponseDTO updateNote(Long id, @Valid CreateNotesRequestDTO request, String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));

        Notes note = notesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Notes Found"));

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());

        return utilDto.convertNotesToNotsResponseDTO(note);
    }

    public void deleteNote(Long id, String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));

        Notes note = notesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Notes Found"));

        notesRepository.delete(note);
    }
}

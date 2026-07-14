package com.mallya.notesapi.utils;

import com.mallya.notesapi.dto.note.NotesResponseDTO;
import com.mallya.notesapi.dto.user.register.UserRegisterResponseDTO;
import com.mallya.notesapi.model.Notes;
import com.mallya.notesapi.model.Users;
import org.springframework.stereotype.Component;

@Component
public class UtilDto {

    public UserRegisterResponseDTO convertUserToUserResponseDTO(Users user){
        UserRegisterResponseDTO userRegisterResponseDTO = new UserRegisterResponseDTO();
        userRegisterResponseDTO.setId(user.getId());
        userRegisterResponseDTO.setEmail(user.getEmail());
        userRegisterResponseDTO.setName(user.getName());
        return userRegisterResponseDTO;
    }

    public NotesResponseDTO convertNotesToNotsResponseDTO(Notes note){
        NotesResponseDTO notesResponseDTO = new NotesResponseDTO();
        notesResponseDTO.setId(note.getId());
        notesResponseDTO.setTitle(note.getTitle());
        notesResponseDTO.setContext(note.getContent());
        notesResponseDTO.setUserId(note.getUser().getId());
        return notesResponseDTO;
    }
}

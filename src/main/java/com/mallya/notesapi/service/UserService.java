package com.mallya.notesapi.service;

import com.mallya.notesapi.dto.UserRequestDTO;
import com.mallya.notesapi.dto.UserResponseDTO;
import com.mallya.notesapi.model.Users;
import com.mallya.notesapi.repository.UserRepository;
import com.mallya.notesapi.utils.UtilDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UtilDto utilDto;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO registerUser(@Valid UserRequestDTO requestDTO) {
        Users user = new Users();
        if(userRepository.existsByEmail(requestDTO.getEmail())){
            throw new IllegalArgumentException("This email is already registered");
        }
        String hashedPassword = passwordEncoder.encode(requestDTO.getPassword());
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(hashedPassword);

        return utilDto.convertUserToUserResponseDTO(userRepository.save(user));

    }
}

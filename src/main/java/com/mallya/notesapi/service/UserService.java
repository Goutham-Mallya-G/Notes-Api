package com.mallya.notesapi.service;

import com.mallya.notesapi.dto.UserLoginRequestDTO;
import com.mallya.notesapi.dto.UserLoginResponseDTO;
import com.mallya.notesapi.dto.UserRegisterRequestDTO;
import com.mallya.notesapi.dto.UserRegisterResponseDTO;
import com.mallya.notesapi.model.Users;
import com.mallya.notesapi.repository.UserRepository;
import com.mallya.notesapi.utils.UtilDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UtilDto utilDto;
    private final PasswordEncoder passwordEncoder;

    public UserRegisterResponseDTO registerUser(@Valid UserRegisterRequestDTO requestDTO) {
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

    public List<UserRegisterResponseDTO> getAllUsers() {
        List<UserRegisterResponseDTO> list = new ArrayList<>();
        for(Users user : userRepository.findAll()){
            list.add(utilDto.convertUserToUserResponseDTO(user));
        }
        return list;
    }

    public UserLoginResponseDTO login(@Valid UserLoginRequestDTO requestDTO) {
        Users user = userRepository.findByEmail(requestDTO.getEmail());
        if(user == null){
            throw new IllegalArgumentException("Invalid email or password");
        }

        boolean isPasswordMatch = passwordEncoder.matches(requestDTO.getPassword() , user.getPassword());
        if(isPasswordMatch){
            return new UserLoginResponseDTO("User logged in");
        }
        throw new IllegalArgumentException("Invalid email or password");
    }
}

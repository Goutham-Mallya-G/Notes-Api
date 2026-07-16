package com.mallya.notesapi.service;

import com.mallya.notesapi.dto.user.login.UserLoginRequestDTO;
import com.mallya.notesapi.dto.user.register.UserRegisterRequestDTO;
import com.mallya.notesapi.dto.user.register.UserRegisterResponseDTO;
import com.mallya.notesapi.model.Category;
import com.mallya.notesapi.model.Users;
import com.mallya.notesapi.repository.UserRepository;
import com.mallya.notesapi.utils.UtilDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtServices;

    public UserRegisterResponseDTO registerUser(@Valid UserRegisterRequestDTO requestDTO) {
        Users user = new Users();
        if(userRepository.existsByEmail(requestDTO.getEmail())){
            throw new IllegalArgumentException("This email is already registered");
        }
        String hashedPassword = passwordEncoder.encode(requestDTO.getPassword());
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setCategories(new ArrayList<>());
        user.setPassword(hashedPassword);

        return utilDto.convertUserToUserResponseDTO(userRepository.save(user));

    }

    public Page<UserRegisterResponseDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Users> usersPage = userRepository.findAll(pageable);
        return usersPage.map(utilDto::convertUserToUserResponseDTO);
    }

    public String login(@Valid UserLoginRequestDTO requestDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(requestDTO.getEmail(),requestDTO.getPassword()));

        if(authentication.isAuthenticated()){
            return jwtServices.generateToken(requestDTO.getEmail());
        }else{
            return null;
        }
    }
}

package com.mallya.notesapi.service;

import com.mallya.notesapi.model.UserPrincipal;
import com.mallya.notesapi.model.Users;
import com.mallya.notesapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(username);

        if(user == null){
            throw new UsernameNotFoundException("No user found");
        }

        return new UserPrincipal(user);
    }
}

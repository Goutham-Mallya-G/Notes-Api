package com.mallya.notesapi.repository;

import com.mallya.notesapi.model.Users;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Boolean existsByEmail(@Email(message = "Invalid email") String email);
    Users findByEmail(@Email(message = "Invalid email")String email);
}

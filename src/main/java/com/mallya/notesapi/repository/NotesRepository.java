package com.mallya.notesapi.repository;

import com.mallya.notesapi.model.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotesRepository extends JpaRepository<Notes, Long> {
    public List<Notes> findByUserEmail(String email);

    Optional<Notes> findByUserEmailAndId(String email, Long id);
}

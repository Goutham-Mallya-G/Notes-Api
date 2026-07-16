package com.mallya.notesapi.repository;

import com.mallya.notesapi.model.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotesRepository extends JpaRepository<Notes, Long> {

    Optional<Notes> findByUserEmailAndIdAndDeletedFalse(String email, Long id);

    List<Notes> findByUserEmailAndArchivedFalseAndDeletedFalse(String email);

    Optional<Notes> findByUserEmailAndIdAndAndArchivedTrue(String email, Long id);

    Optional<Notes> findByUserEmailAndIdAndDeletedTrue(String email, Long id);

    List<Notes> findByUserEmailAndDeletedFalseAndArchivedFalseAndTitleContainingIgnoreCase(String email, String title);

    List<Notes> findByUserEmailAndFavoriteTrueAndDeletedFalse(String email);

    List<Notes> findByUserEmailAndArchivedTrueAndDeletedFalse(String email);
}

package com.mallya.notesapi.repository;

import com.mallya.notesapi.model.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotesRepository extends JpaRepository<Notes, Long> {

    Optional<Notes> findByUserEmailAndIdAndDeletedFalse(String email, Long id);

    Page<Notes> findByUserEmailAndArchivedFalseAndDeletedFalse(String email, Pageable pageable);

    Optional<Notes> findByUserEmailAndIdAndArchivedTrue(String email, Long id);

    Optional<Notes> findByUserEmailAndIdAndDeletedTrue(String email, Long id);

    Page<Notes> findByUserEmailAndDeletedFalseAndArchivedFalseAndTitleContainingIgnoreCase(String email, String title, Pageable pageable);

    Page<Notes> findByUserEmailAndFavoriteTrueAndDeletedFalse(String email, Pageable pageable);

    Page<Notes> findByUserEmailAndArchivedTrueAndDeletedFalse(String email, Pageable pageable);

    Page<Notes> findByUserEmailAndDeletedFalseAndArchivedFalseAndContentContainingIgnoreCase(String email, String content, Pageable pageable);

    @Query("""
        SELECT n
        FROM Notes n
        WHERE n.user.email = :email
          AND n.deleted = false
          AND n.archived = false
          AND (
                LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%'))
             OR LOWER(n.content) LIKE LOWER(CONCAT('%', :query, '%'))
          )
    """)
    Page<Notes> searchNotes(@Param("email") String email,
                            @Param("query") String query,
                            Pageable pageable);

    Page<Notes> findByUserEmailAndDeletedTrueAndTitleContainingIgnoreCase(String email, String title,Pageable pageable);

    Page<Notes> findByUserEmailAndDeletedTrueAndContentContainingIgnoreCase(String email, String content, Pageable pageable);

    @Query("""
        SELECT n
        FROM Notes n
        WHERE n.user.email = :email
          AND n.deleted = true
          AND (
                LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%'))
             OR LOWER(n.content) LIKE LOWER(CONCAT('%', :query, '%'))
          )
    """)
    Page<Notes> searchDeletedNotes(String email, String query, Pageable pageable);

    List<Notes> findByUserEmailAndDeletedTrue(String email);

    List<Notes> findByDeletedTrueAndDeletedAtBefore(LocalDateTime deletedAt);
}

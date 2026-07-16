package com.mallya.notesapi.repository;

import com.mallya.notesapi.model.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotesRepository extends JpaRepository<Notes, Long> {

    Optional<Notes> findByUserEmailAndIdAndDeletedFalse(String email, Long id);

    List<Notes> findByUserEmailAndArchivedFalseAndDeletedFalse(String email);

    Optional<Notes> findByUserEmailAndIdAndArchivedTrue(String email, Long id);

    Optional<Notes> findByUserEmailAndIdAndDeletedTrue(String email, Long id);

    List<Notes> findByUserEmailAndDeletedFalseAndArchivedFalseAndTitleContainingIgnoreCase(String email, String title);

    List<Notes> findByUserEmailAndFavoriteTrueAndDeletedFalse(String email);

    List<Notes> findByUserEmailAndArchivedTrueAndDeletedFalse(String email);

    List<Notes> findByUserEmailAndDeletedFalseAndArchivedFalseAndContentContainingIgnoreCase(String email, String content);

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
    List<Notes> searchNotes(@Param("email") String email,
                            @Param("query") String query);

    List<Notes> findByUserEmailAndDeletedTrueAndTitleContainingIgnoreCase(String email, String title);

    List<Notes> findByUserEmailAndDeletedTrueAndAndContentContainingIgnoreCase(String email, String content);

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
    List<Notes> searchDeletedNotes(String email, String query);

    List<Notes> findByUserEmailAndDeletedTrue(String email);

    List<Notes> findByDeletedTrueAndDeletedAtBefore(LocalDateTime deletedAt);
}

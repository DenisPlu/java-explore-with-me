package ru.practicum.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT * FROM comments WHERE user_id = ?1 AND event_id = ?2", nativeQuery = true)
    Comment getByUserAndEventId(Long userId, Long eventId);

    @Query(value = "SELECT * FROM comments WHERE user_id = ?1 ORDER BY id DESC LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<Comment> getByUserId(Long userId, Integer size, Integer from);

    @Query(value = "SELECT * FROM comments WHERE event_id = ?1 ORDER BY id DESC LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<Comment> getByEventId(Long eventId, Integer size, Integer from);
}
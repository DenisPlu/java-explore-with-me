package ru.practicum.comment;

import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentNewDto;
import ru.practicum.comment.dto.CommentUpdateDto;

import java.util.List;

public interface CommentService {
    CommentFullDto create(Long userId, CommentNewDto commentNewDto);

    List<CommentFullDto> getByUserId(Long userId, Integer size, Integer from);

    List<CommentFullDto> getByEventId(Long eventId, Integer size, Integer from);

    CommentFullDto updateByUser(Long userId, Long commentId, CommentUpdateDto commentUpdateDto);

    CommentFullDto updateByAdmin(Long commentId, CommentUpdateDto commentUpdateDto);

    void delete(Long commentId);
}
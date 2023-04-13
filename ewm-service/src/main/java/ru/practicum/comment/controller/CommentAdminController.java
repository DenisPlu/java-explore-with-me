package ru.practicum.comment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.CommentService;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentUpdateDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
public class CommentAdminController {

    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    public CommentFullDto updateByAdmin(@PathVariable Long commentId,
                                       @RequestBody CommentUpdateDto commentUpdateDto) throws JSONException, JsonProcessingException {
        log.info("Received a request from Admin to update a Comment with id: {} CommentUpdateDto: {}", commentId, commentUpdateDto);
        return commentService.updateByAdmin(commentId, commentUpdateDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long commentId) throws JSONException, JsonProcessingException {
        log.info("Received a request from Admin to delete a Comment with id: {}", commentId);
        commentService.delete(commentId);
    }
}
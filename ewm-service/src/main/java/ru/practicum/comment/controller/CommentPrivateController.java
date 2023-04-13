package ru.practicum.comment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.CommentService;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentNewDto;
import ru.practicum.comment.dto.CommentUpdateDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
public class CommentPrivateController {

    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommentFullDto create(@PathVariable Long userId, @RequestBody @Valid CommentNewDto commentNewDto) {
        log.info("Received a request from user with id {} to create a new Comment: {}", userId, commentNewDto);
        return commentService.create(userId, commentNewDto);
    }

    @PatchMapping("/{commentId}")
    public CommentFullDto updateByUser(@PathVariable Long userId,
                                       @PathVariable Long commentId,
                                       @RequestBody CommentUpdateDto commentUpdateDto) throws JSONException, JsonProcessingException {
        log.info("Received a request from User with id {} to update a Comment with id: {} CommentUpdateDto: {}", userId, commentId, commentUpdateDto);
        return commentService.updateByUser(userId, commentId, commentUpdateDto);
    }
}
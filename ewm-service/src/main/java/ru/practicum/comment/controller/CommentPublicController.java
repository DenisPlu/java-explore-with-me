package ru.practicum.comment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.CommentService;
import ru.practicum.comment.dto.CommentFullDto;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
public class CommentPublicController {

    private final CommentService commentService;

    @GetMapping("/{userId}")
    public List<CommentFullDto> getByUserId(@PathVariable Long userId,
                                            @RequestParam(defaultValue = "10") @Positive Integer size,
                                            @RequestParam(defaultValue = "0") @Positive Integer from) throws JSONException, JsonProcessingException {
        log.info("Received a request to get Comments of User with id {} size {} from {}", userId, size, from);
        return commentService.getByUserId(userId, size, from);
    }

    @GetMapping("/{eventId}")
    public List<CommentFullDto> getByEventId(@PathVariable Long eventId,
                                     @RequestParam(defaultValue = "10") @Positive Integer size,
                                     @RequestParam(defaultValue = "0") @Positive Integer from) {
        log.info("Received a request to get Comments of Event with id {} size {} from {}", eventId, size, from);
        return commentService.getByEventId(eventId, size, from);
    }
}
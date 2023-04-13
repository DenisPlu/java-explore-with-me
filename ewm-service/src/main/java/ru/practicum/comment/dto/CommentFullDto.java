package ru.practicum.comment.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.comment.model.CommentState;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentFullDto {

    String text;

    LocalDateTime created;

    LocalDateTime updated;

    String eventTitle;

    String authorName;

    CommentState status;
}
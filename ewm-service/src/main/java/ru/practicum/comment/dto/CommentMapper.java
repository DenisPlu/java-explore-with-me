package ru.practicum.comment.dto;

import lombok.NoArgsConstructor;
import ru.practicum.comment.model.Comment;

@NoArgsConstructor
public final class CommentMapper {

    public static CommentFullDto toCommentFullDtoFromComment(
            Comment comment, String eventTitle, String authorName) {
        return new CommentFullDto(
                comment.getText(),
                comment.getCreated(),
                comment.getUpdated(),
                eventTitle,
                authorName,
                comment.getStatus()
        );
    }
}
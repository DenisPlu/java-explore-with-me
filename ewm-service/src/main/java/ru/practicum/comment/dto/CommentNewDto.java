package ru.practicum.comment.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentNewDto {

    Long id;

    @NotEmpty
    @Size(max = 2000)
    @Size(min = 10)
    String text;

    Long eventId;
}
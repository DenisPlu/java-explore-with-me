package ru.practicum.comment.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String text;

    LocalDateTime created;

    LocalDateTime updated;

    @Column(name = "event_id")
    Long eventId;

    @Column(name = "user_id")
    Long authorId;

    @Enumerated(EnumType.STRING)
    CommentState status;
}

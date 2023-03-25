package ru.practicum.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotEmpty(message = "{validation.name.NotEmpty}")
    @Size(max = 120)
    @Size(min = 3)
    String title;

    @NotEmpty(message = "{validation.name.NotEmpty}")
    @Size(max = 7000)
    @Size(min = 20)
    String description;

    @NotEmpty(message = "{validation.name.NotEmpty}")
    @Size(max = 2000)
    @Size(min = 20)
    String annotation;

    @Enumerated(EnumType.STRING)
    EventState state;

    Integer category;

    LocalDateTime createdOn;

    // дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
    //Создать кастомный валидатор
    @Future(message = "{validation.birthDate.Future}")
    LocalDateTime eventDate;

    @Future(message = "{validation.birthDate.Future}")
    LocalDateTime publishedOn;

    Long location;

    Long initiator;

    boolean paid;

    Integer participantLimit;

    boolean requestModeration;

    // Integer views; - забирать из сервера статистики для ДТО?
}

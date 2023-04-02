package ru.practicum.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "events")
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

    Integer categoryId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", shape = JsonFormat.Shape.STRING)
    LocalDateTime createdOn;

    // дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
    //Создать кастомный валидатор
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", shape = JsonFormat.Shape.STRING)
    LocalDateTime eventDate;

    //@Column(name = "publishedOn")
    //@DateTimeFormat(iso = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", shape = JsonFormat.Shape.STRING)
    @Future(message = "{validation.birthDate.Future}")
    LocalDateTime publishedOn;

    Integer confirmedRequests;

    Long locationId;

    Long initiatorId;

    boolean paid;

    Integer participantLimit;

    boolean requestModeration;

    // Integer views; - забирать из сервера статистики для ДТО?
}

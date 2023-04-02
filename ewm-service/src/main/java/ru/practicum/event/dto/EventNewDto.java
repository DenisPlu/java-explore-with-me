package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.location.Location;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventNewDto {

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

    Integer category;

    // дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
    //Создать кастомный валидатор
    String eventDate;

    Location location;

    boolean paid;

    Integer participantLimit;

    boolean requestModeration;
}

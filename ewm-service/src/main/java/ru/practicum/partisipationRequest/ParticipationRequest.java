package ru.practicum.partisipationRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.EventState;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    LocalDateTime created;

    Long event;

    Long requester;

    @Enumerated(EnumType.STRING)
    RequestState status;
}

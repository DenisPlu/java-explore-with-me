package ru.practicum.event.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventNewDto;
import ru.practicum.event.location.Location;
import ru.practicum.event.location.LocationDto;
import ru.practicum.event.location.LocationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventRepository;
import ru.practicum.user.UserRepository;
import ru.practicum.user.UserShortDto;

import java.time.LocalDateTime;

public class EventMapper {

    public EventMapper() {}

    public static Event toEventFromEventNewDto(Long userId, Long locationId, EventNewDto eventNewDto) {
        return new Event(
                eventNewDto.getId(),
                eventNewDto.getTitle(),
                eventNewDto.getDescription(),
                eventNewDto.getAnnotation(),
                EventState.valueOf("WAITING"),
                eventNewDto.getCategory(),
                LocalDateTime.now(),
                LocalDateTime.parse(eventNewDto.getEventDate().replaceAll(" ", "T")),
                null,
                0,
                locationId,
                userId,
                eventNewDto.isPaid(),
                eventNewDto.getParticipantLimit(),
                eventNewDto.isRequestModeration()
        );
    }

    public static EventFullDto toEventFullDtoFromEvent(
            Event event, Category category, LocationDto locationDto, UserShortDto userShortDto, Integer views) {
        return new EventFullDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getAnnotation(),
                event.getState(),
                category,
                event.getCreatedOn(),
                event.getEventDate().toString().replaceAll("T", " "),
                event.getPublishedOn(),
                event.getConfirmedRequests(),
                locationDto,
                userShortDto,
                event.isPaid(),
                event.getParticipantLimit(),
                event.isRequestModeration(),
                views
        );
    }
    public static EventShortDto toEventShortDtoFromEvent(
            Event event, Category category, UserShortDto userShortDto, Integer views) {
        return new EventShortDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getAnnotation(),
                category,
                event.getEventDate().toString().replaceAll("T", " "),
                userShortDto,
                event.isPaid(),
                views
        );
    }
}

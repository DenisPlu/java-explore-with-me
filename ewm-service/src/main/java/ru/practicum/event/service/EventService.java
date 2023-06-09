package ru.practicum.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventNewDto;
import ru.practicum.event.dto.EventUpdateDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto create(Long id, EventNewDto eventNewDto);

    List<EventFullDto> searchEventsPublic(String text, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                          boolean onlyAvailable, List<Integer> categories, String sort,
                                          Integer from, Integer size, String endpointPath) throws JSONException, JsonProcessingException;

    EventFullDto getByEventId(Long id, String endpointPath);

    List<EventFullDto> getByUserId(Long id, Integer size, Integer from) throws JSONException, JsonProcessingException;

    EventFullDto getByUserAndEventId(Long userId, Long eventId, Integer size, Integer from) throws JSONException, JsonProcessingException;

    EventFullDto updateByUser(Long userId, Long eventId, EventUpdateDto eventUpdateDto) throws JSONException, JsonProcessingException;

    List<EventFullDto> searchEventsByAdmin(List<Long> usersId, List<String> states, List<Integer> categories,
                                           LocalDateTime startTime, LocalDateTime endTime,
                                           Integer from, Integer size) throws JSONException, JsonProcessingException;

    EventFullDto updateByAdmin(Long eventId, EventUpdateDto eventUpdateDto) throws JSONException, JsonProcessingException;
}
package ru.practicum.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventServiceImpl;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventAdminController {

    private final EventServiceImpl eventService;
    // поиск и редактирование евента

    @GetMapping
    public List<EventFullDto> searchEventsByAdmin(
            @RequestParam(defaultValue = "") List<Long> users,
            @RequestParam(defaultValue = "") List<String> states,
            @RequestParam(defaultValue = "") List<Integer> categories,
            @RequestParam(defaultValue = "2000-01-01 19:30:35.544") String rangeStart,
            @RequestParam(defaultValue = "2050-01-01 19:30:35.544") String rangeEnd,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            @RequestParam(defaultValue = "0") @Positive Integer from) throws JSONException, JsonProcessingException {
        log.info("Received a request to get Events size {} from {} ", size, from);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startTime = LocalDateTime.parse(rangeStart.replaceAll(" ", "T"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(rangeEnd.replaceAll(" ", "T"), formatter);
        return eventService.searchEventsByAdmin(users, states, categories, startTime, endTime, size, from);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long eventId,
                               @RequestBody EventUpdateDto eventUpdateDto) throws JSONException, JsonProcessingException {
        log.info("Received a request to update Event with id {} Event:{} ", eventId, eventUpdateDto);
        return eventService.updateByAdmin(eventId, eventUpdateDto);
    }


}

package ru.practicum.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.service.EventServiceImpl;
import ru.practicum.user.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventPublicController {

    private final UserServiceImpl userService;

    private final EventServiceImpl eventService;

    @GetMapping
    public List<EventFullDto> searchEventsPublic(
            @RequestParam(defaultValue = "") String text,
            @RequestParam(defaultValue = "") String paid,
            @RequestParam(defaultValue = "2000-01-01 19:30:35.544") String rangeStart,
            @RequestParam(defaultValue = "2050-01-01 19:30:35.544") String rangeEnd,
            @RequestParam(defaultValue = "false") String onlyAvailable,
            @RequestParam(defaultValue = "") List<Integer> categories,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            @RequestParam(defaultValue = "0") @Positive Integer from,
            HttpServletRequest request) throws JSONException, JsonProcessingException {
        log.info("Received a request to get Events size {} from {} ", size, from);
        String endpointPath = request.getRequestURI();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startTime = LocalDateTime.parse(rangeStart.replaceAll(" ", "T"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(rangeEnd.replaceAll(" ", "T"), formatter);
        return eventService.searchEventsPublic(text, Boolean.getBoolean(paid), startTime, endTime,
                Boolean.getBoolean(onlyAvailable), categories, sort, size, from, endpointPath);
    }

    @GetMapping("/{id}")
    public EventFullDto getByEventId(@PathVariable Long id,
                                     HttpServletRequest request) {
        log.info("Received a request to get Event with id {} ", id);
        String endpointPath = request.getRequestURI();
        return eventService.getByEventId(id, endpointPath);
    }
}

package ru.practicum.partisipationRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventNewDto;
import ru.practicum.event.service.EventServiceImpl;
import ru.practicum.partisipationRequest.Request;
import ru.practicum.partisipationRequest.RequestServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events/{eventId}/requests")
public class EventPrivateRequestController {

    private final RequestServiceImpl requestService;

    @PatchMapping
    public List<Request> updateRequestsStatus(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @RequestBody @Valid RequestUpdateDto requestUpdateDto) {
        log.info("Received a request from user with id {} to update events: {}, requestUpdateDto: {}", userId, eventId, requestUpdateDto);
        return requestService.updateRequestsStatus(userId, eventId, requestUpdateDto);
    }

    @GetMapping
    public List<Request> getByUserAndEventId(@PathVariable Long userId,
                                             @PathVariable Long eventId) {
        log.info("Received a request to get Requests of User with id {} in Event with id {}", userId, eventId);
        return requestService.getByUserAndEventId(userId, eventId);
    }
}
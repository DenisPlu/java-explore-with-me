package ru.practicum.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.location.LocationDto;
import ru.practicum.event.location.LocationMapper;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventNewDto;
import ru.practicum.event.location.LocationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.StateAction;
import ru.practicum.hit.HitDto;
import ru.practicum.user.UserMapper;
import ru.practicum.user.UserRepository;
import ru.practicum.user.UserShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;

    private final LocationRepository locationRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final StatsClient statsClient;

    private final HitClient hitClient;

    private final HitsClient hitsClient;

    @Override
    public EventFullDto create(Long userId, EventNewDto eventNewDto) {
        Long locationId = locationRepository.save(eventNewDto.getLocation()).getId();
        LocationDto locationDto = LocationMapper.toLocationDtoFromLocation(eventNewDto.getLocation());
        Event event = eventRepository.save(EventMapper.toEventFromEventNewDto(userId, locationId, eventNewDto));
        Category category = categoryRepository.getReferenceById(event.getCategoryId());
        UserShortDto userShortDto = UserMapper.toUserShortDtoFromUser(userRepository.getReferenceById(userId));

        return getEventWithViews(event, locationDto, category, userShortDto);
    }

    @Override
    public List<EventFullDto> getByUserId(Long id, Integer size, Integer from) throws JSONException, JsonProcessingException {
        List<Event> events = eventRepository.getByUserIdWithPagination(id, size, from);
        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        System.out.println(events);
        for (Event event: events){
            eventFullDtoList.add(toEventFullDtoFromEvent(event));
        }
        return eventFullDtoList;
    }

    @Override
    public EventFullDto getByUserAndEventId(Long userId, Long eventId, Integer size, Integer from) throws JSONException, JsonProcessingException {
        Event event = eventRepository.getByUserAndEventId(userId, eventId, size, from);
        return toEventFullDtoFromEvent(event);
    }

    private EventFullDto toEventFullDtoFromEvent(Event event) throws JSONException, JsonProcessingException {
        LocationDto locationDto = LocationMapper.toLocationDtoFromLocation(locationRepository.getReferenceById(event.getLocationId()));
        Category category = categoryRepository.getReferenceById(event.getCategoryId());
        UserShortDto userShortDto = UserMapper.toUserShortDtoFromUser(userRepository.getReferenceById(event.getInitiatorId()));

        return getEventWithViews(event, locationDto, category, userShortDto);
    }

    private EventFullDto getEventWithViews(Event event, LocationDto locationDto, Category category, UserShortDto userShortDto) {
        String uriEvent = "/events/" + event.getId().toString();
        List<HitDto> hitDtos = statsClient.getStats(uriEvent);
        Integer views = 0;
        if (!hitDtos.isEmpty()){
            views = hitDtos.get(0).getHits();
        }

        return EventMapper.toEventFullDtoFromEvent(event, category, locationDto, userShortDto, views);
    }

    @Override
    public List<EventFullDto> searchEventsPublic(String text, boolean paid, LocalDateTime startTime, LocalDateTime endTime,
                                            boolean onlyAvailable, List<Integer> categories, String sort,
                                            Integer size, Integer from, String endpointPath) throws JSONException, JsonProcessingException {
        List<Event> events;
        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        if (onlyAvailable){
            events = eventRepository.searchEventsPublicOnlyAvailable(text, paid, startTime, endTime, categories, sort, size, from);
            for(Event event: events){
                eventFullDtoList.add(toEventFullDtoFromEvent(event));
                hitsClient.createHit("ewm-main-service", endpointPath);
            }
        } else {
            events = eventRepository.searchEventsPublic(text, paid, startTime, endTime, categories, sort.toLowerCase(), size, from);
            for(Event event: events){
                eventFullDtoList.add(toEventFullDtoFromEvent(event));
                hitsClient.createHit("ewm-main-service", endpointPath);
            }
        }
        return eventFullDtoList;
    }

    @Override
    public EventFullDto getByEventId(Long id, String endpointPath) {
        try {
            eventRepository.getByIdIfPublished(id).getTitle();
            Event event = eventRepository.getByIdIfPublished(id);
            hitsClient.createHit("ewm-main-service", endpointPath);
            return toEventFullDtoFromEvent(event);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event с запрошенным id не существует");
        }
    }

    @Override
    public EventFullDto updateByUser(Long userId, Long eventId, EventUpdateDto eventUpdateDto) throws JSONException, JsonProcessingException {
        Event event = eventRepository.getReferenceById(eventId);
        LocalDateTime startTime = event.getEventDate();
        //System.out.println(event.isRequestModeration());
        //System.out.println(startTime.isAfter(LocalDateTime.now().plusHours(2)));
        //System.out.println(event.getInitiatorId().equals(userId));
        if (startTime.isAfter(LocalDateTime.now().plusHours(2)) && event.isRequestModeration() && event.getInitiatorId().equals(userId)){
            checkAndUpdateEvent(eventUpdateDto, event);
            if (Optional.ofNullable(eventUpdateDto.getStateAction()).isPresent()){
                if (eventUpdateDto.getStateAction().equals(StateAction.SEND_TO_REVIEW)){
                    event.setState(EventState.WAITING);
                }
                if (eventUpdateDto.getStateAction().equals(StateAction.CANCEL_REVIEW)){
                    event.setState(EventState.PUBLISHED);
                }
            }
            return toEventFullDtoFromEvent(eventRepository.save(event));
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Событие должно быть в состоянии ожидания");
        }
    }

    private void checkAndUpdateEvent(EventUpdateDto eventUpdateDto, Event event) {
        if (Optional.ofNullable(eventUpdateDto.getTitle()).isPresent()){
            event.setTitle(eventUpdateDto.getTitle());
        }
        if (Optional.ofNullable(eventUpdateDto.getDescription()).isPresent()){
            event.setDescription(eventUpdateDto.getDescription());
        }
        if (Optional.ofNullable(eventUpdateDto.getAnnotation()).isPresent()){
            event.setAnnotation(eventUpdateDto.getAnnotation());
        }
        if (Optional.ofNullable(eventUpdateDto.getCategory()).isPresent()){
            event.setCategoryId(eventUpdateDto.getCategory());
        }
        if (Optional.ofNullable(eventUpdateDto.getEventDate()).isPresent()){
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime eventDate = LocalDateTime.parse(eventUpdateDto.getEventDate().replaceAll(" ", "T"), formatter);
            event.setEventDate(eventDate);
        }
        if (Optional.ofNullable(eventUpdateDto.getLocation()).isPresent()){
            event.setLocationId(locationRepository.save(eventUpdateDto.getLocation()).getId());
        }
        if (Optional.ofNullable(eventUpdateDto.getPaid()).isPresent()){
            event.setPaid(Boolean.parseBoolean(eventUpdateDto.getPaid()));
        }
        if (Optional.ofNullable(eventUpdateDto.getParticipantLimit()).isPresent()){
            event.setParticipantLimit(eventUpdateDto.getParticipantLimit());
        }
        if (Optional.ofNullable(eventUpdateDto.getRequestModeration()).isPresent()){
            event.setRequestModeration(Boolean.parseBoolean(eventUpdateDto.getRequestModeration()));
        }
    }

    @Override
    public List<EventFullDto> searchEventsByAdmin(List<Long> usersId, List<String> states, List<Integer> categories,
                                                  LocalDateTime startTime, LocalDateTime endTime,
                                                  Integer size, Integer from) throws JSONException, JsonProcessingException {
        List<Event> events;
        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        events = eventRepository.searchEventsByAdmin(usersId, states, categories, startTime, endTime, size, from);
        for(Event event: events){
            eventFullDtoList.add(toEventFullDtoFromEvent(event));
        }
        return eventFullDtoList;
    }

    @Override
    public EventFullDto updateByAdmin(Long eventId, EventUpdateDto eventUpdateDto) throws JSONException, JsonProcessingException {
        Event event = eventRepository.getReferenceById(eventId);
        LocalDateTime startTime = event.getEventDate();
        if (startTime.isAfter(LocalDateTime.now().plusHours(1)) && event.getState().equals(EventState.WAITING)){
            checkAndUpdateEvent(eventUpdateDto, event);
            if (Optional.ofNullable(eventUpdateDto.getStateAction()).isPresent()){
                if (eventUpdateDto.getStateAction().equals(StateAction.PUBLISH_EVENT)){
                    event.setState(EventState.PUBLISHED);
                }
                if (eventUpdateDto.getStateAction().equals(StateAction.REJECT_EVENT)){
                    event.setState(EventState.CANCELED);
                }
            }
            return toEventFullDtoFromEvent(eventRepository.save(event));
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Событие должно быть в состоянии ожидания");
        }
    }
}
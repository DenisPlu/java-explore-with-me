package ru.practicum.partisipationRequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Getter
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {


    private final RequestRepository requestRepository;

    private final EventRepository eventRepository;


    @Override
    public Request create(Long userId, Long eventId) {
        System.out.println("userId = " + userId + " eventId = " + eventId);
        boolean isExists = !getByUserAndEventId(userId, eventId).isEmpty();
        System.out.println(getByUserAndEventId(userId, eventId));
        System.out.println(getByUserAndEventId(userId, eventId).isEmpty());
        System.out.println("isExists = " + isExists);

        boolean isYourEvent = eventRepository.getReferenceById(eventId).getInitiatorId().equals(userId);
        //System.out.println("isYourEvent = " + isYourEvent);

        boolean isEventPublished = eventRepository.getReferenceById(eventId).getState().equals(EventState.PUBLISHED);
        //System.out.println("isEventPublished = " + isEventPublished);

        boolean isParticipationLimitGot = eventRepository.getReferenceById(eventId).getParticipantLimit()
                <= eventRepository.getReferenceById(eventId).getConfirmedRequests();
        System.out.println("eventRepository.getReferenceById(eventId).getParticipantLimit()" + eventRepository.getReferenceById(eventId).getParticipantLimit());
        System.out.println("eventRepository.getReferenceById(eventId).getConfirmedRequests()" + eventRepository.getReferenceById(eventId).getConfirmedRequests());
        System.out.println("isParticipationLimitGot = " + isParticipationLimitGot);

        boolean isModerateOn = eventRepository.getReferenceById(eventId).isRequestModeration();

        //System.out.println("isModerateOn = " + isModerateOn);

        System.out.println(!isExists && !isYourEvent && isEventPublished && !isParticipationLimitGot);


        if (!isExists && !isYourEvent && isEventPublished && !isParticipationLimitGot) {
            Request request;
            if (!isModerateOn) {
                request = new Request(
                        null,
                        LocalDateTime.now(),
                        eventId,
                        userId - 1,
                        RequestState.valueOf("CONFIRMED")
                );
            } else {
                request = new Request(
                        null,
                        LocalDateTime.now(),
                        eventId,
                        userId - 1,
                        RequestState.valueOf("PENDING")
                );
            }
            Event event = eventRepository.getReferenceById(eventId);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            //System.out.println(eventRepository.save(event));
            return requestRepository.save(request);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Участие в Событии не удовлетворяет правилам создания");
        }
    }

    @Override
    public List<Request> get(Long userId) {
        return requestRepository.getAllByUserId(userId);
    }

    @Override
    public List<Request> getByUserAndEventId(Long userId, Long eventId) {
        return requestRepository.getByUserAndEventId(userId, eventId);
    }

    @Override
    public Request cancelRequestByUser(Long userId, Long requestId) {
        Request request = requestRepository.getReferenceById(requestId);
            request.setStatus(RequestState.CANCELED);
        return requestRepository.save(request);
    }

    @Override
    public RequestUpdateResultDto updateRequestsStatus(Long userId, Long eventId, RequestUpdateDto requestUpdateDto) {
        System.out.println("userId = " + userId + " eventId = " + eventId);
        System.out.println(requestUpdateDto);

        List<Request> requests = requestRepository.getByUserAndEventId(userId, eventId);

        System.out.println(requests);

        RequestUpdateResultDto requestResultList = new RequestUpdateResultDto(new ArrayList<RequestDto>(), new ArrayList<RequestDto>());

        for (Request request : requests) {
            if (requestUpdateDto.getRequestIds().contains(request.getId())) {
                if (requestUpdateDto.getStatus().equals(RequestUpdateState.CONFIRMED)) {
                    request.setStatus(RequestState.CONFIRMED);
                    requestResultList.getConfirmedRequests().add(RequestMapper.toRequestDtoFromRequest(request));
                } else if (requestUpdateDto.getStatus().equals(RequestUpdateState.REJECTED)) {
                    request.setStatus(RequestState.REJECTED);
                    requestResultList.getRejectedRequests().add(RequestMapper.toRequestDtoFromRequest(request));
                }
            }
        }
        System.out.println(requestResultList);
        return requestResultList;
    }
}
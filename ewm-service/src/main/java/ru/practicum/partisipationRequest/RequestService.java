package ru.practicum.partisipationRequest;

import java.util.List;

public interface RequestService {
    Request create(Long userId, Long EventId);

    List<Request> get(Long id);

    List <Request> getByUserAndEventId(Long userId, Long eventId);

    Request update(Long userId, Long requestId);

    List<Request> updateRequestsStatus(Long userId, Long eventId, RequestUpdateDto requestUpdateDto);
}

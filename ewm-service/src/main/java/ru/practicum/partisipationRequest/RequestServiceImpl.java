package ru.practicum.partisipationRequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Getter
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService{


    private final RequestRepository requestRepository;

    @Override
    public Request create(Long userId, Long eventId) {

        Request request = new Request(
                null,
                LocalDateTime.now(),
                eventId,
                userId,
                RequestState.valueOf("PENDING")
        );
        return requestRepository.save(request);
    }

    @Override
    public List<Request> get(Long userId) {
        return requestRepository.getAllByUserId(userId);
    }

    @Override
    public List <Request> getByUserAndEventId(Long userId, Long eventId) {
        return requestRepository.getByUserAndEventId(userId, eventId);
    }

    @Override
    public Request cancelRequestByUser(Long userId, Long requestId) {
        Request request = requestRepository.getReferenceById(requestId);
        if (request.getRequester() == userId) {
            request.setStatus(RequestState.PENDING);
        }
        return requestRepository.save(request);
    }

    @Override
    public List<Request> updateRequestsStatus(Long userId, Long eventId, RequestUpdateDto requestUpdateDto) {
        List<Request> requests = requestRepository.getByUserAndEventId(userId, eventId);
        for (Request request: requests){
            if (requestUpdateDto.getRequestIds().contains(request.getId())){
                if (requestUpdateDto.getStatus().equals(RequestUpdateState.CONFIRMED)){
                    request.setStatus(RequestState.CONFIRMED);
                } else if (requestUpdateDto.getStatus().equals(RequestUpdateState.REJECTED)) {
                    request.setStatus(RequestState.CANCELED);
                }
            }
        }
        return requests;
    }
}
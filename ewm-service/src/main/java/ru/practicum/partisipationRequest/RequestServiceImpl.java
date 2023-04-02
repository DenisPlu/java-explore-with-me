package ru.practicum.partisipationRequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
                userId-1,                                          //!!!!
                RequestState.valueOf("PENDING")
        );
        return requestRepository.save(request);

        //нельзя добавить повторный запрос (Ожидается код ошибки 409)
        //инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)
        //нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)
        //если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)
        //если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
    }

    @Override
    public List<Request> get(Long userId) {
        //try {
            return requestRepository.getAllByUserId(userId);
        //} catch (Exception e) {
            //throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запросов от пользователя с указанным id не существует");
        //}
    }

    @Override
    public List <Request> getByUserAndEventId(Long userId, Long eventId) {
        System.out.println("userId = " + userId + " eventId = " + eventId);
        System.out.println(requestRepository.getByUserAndEventId(userId, eventId));
        return requestRepository.getByUserAndEventId(userId, eventId);
    }

    @Override
    public Request update(Long userId, Long requestId) {
        Request request = requestRepository.getReferenceById(requestId);
        request.setStatus(RequestState.CANCELED);
        return requestRepository.save(request);
    }

    @Override
    public List<Request> updateRequestsStatus(Long userId, Long eventId, RequestUpdateDto requestUpdateDto) {
        //System.out.println("userId = " + userId + " eventId = " + eventId + " requestUpdateDto = " + requestUpdateDto);
        //System.out.println(requestRepository.getByUserAndEventId(userId, eventId));
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

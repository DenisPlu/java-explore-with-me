package ru.practicum.partisipationRequest;

public class RequestMapper {

    public RequestMapper() {
    }

    public static RequestDto toRequestDtoFromRequest(Request request) {
        return new RequestDto(
                request.getId(),
                request.getCreated(),
                request.getEvent(),
                request.getRequester(),
                request.getStatus()
        );
    }
}
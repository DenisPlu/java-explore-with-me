package ru.practicum.partisipationRequest;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RequestMapper {

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
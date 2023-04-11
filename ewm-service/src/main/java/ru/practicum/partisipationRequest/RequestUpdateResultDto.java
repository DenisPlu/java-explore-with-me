package ru.practicum.partisipationRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestUpdateResultDto {
    List<RequestDto> confirmedRequests;
    List<RequestDto> rejectedRequests;
}
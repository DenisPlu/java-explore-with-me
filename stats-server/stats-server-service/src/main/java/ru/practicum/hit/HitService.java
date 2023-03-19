package ru.practicum.hit;

import ru.practicum.HitDto;
import ru.practicum.HitStringDateDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    String create(HitStringDateDto hitDto);

    List<HitDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, String unique);
}
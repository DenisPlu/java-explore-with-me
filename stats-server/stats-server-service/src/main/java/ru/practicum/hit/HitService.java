package ru.practicum.hit;

import ru.practicum.HitDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    String create(Hit hit);

    List<HitDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, String unique);
}
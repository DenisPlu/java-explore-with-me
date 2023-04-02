package ru.practicum.hit;

import ru.practicum.HitDto;
import ru.practicum.HitDtoMin;

import java.time.LocalDateTime;

public final class HitMapper {
    private HitMapper(){};

    public static HitDto toHitDto(Hit hit, Long hitsNumber) {

        return new HitDto(
                hit.getApp(),
                hit.getUri(),
                hitsNumber
        );
    }

    public static Hit toHitFromHitDtoMin(HitDtoMin hitDtoMin, String ip) {

        return new Hit(
                hitDtoMin.getId(),
                hitDtoMin.getApp(),
                hitDtoMin.getUri(),
                ip,
                LocalDateTime.now()
        );
    }
}
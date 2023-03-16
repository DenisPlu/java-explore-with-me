package ru.practicum.hit;

import lombok.RequiredArgsConstructor;
import ru.practicum.HitDto;
import ru.practicum.HitStringDateDto;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class HitMapper {

    public static HitDto toHitDto(Hit hit, Long hitsNumber) {

        return new HitDto(
                hit.getApp(),
                hit.getUri(),
                hitsNumber
        );
    }

    public static Hit toHitFromHitStringDateDto(HitStringDateDto hitDto) {

        return new Hit(
                hitDto.getId(),
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                LocalDateTime.now()
        );
    }
}
package ru.practicum.event.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.HitDto;

import java.util.Arrays;
import java.util.List;

@Component
public class StatsClient {

    private static final String API_path = "http://localhost:9090" + "/stats" + "?uris=";

    public List<HitDto> getStats(String uris) {
        RestTemplate rest = new RestTemplate();
        HitDto[] forNow = rest.getForObject(API_path + uris, HitDto[].class);
        List<HitDto> hitDtos = Arrays.asList(forNow);
        return hitDtos;
    }
}
package ru.practicum.event.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.HitDto;

import java.util.Arrays;
import java.util.List;

@Component
public class StatsClient {

    @Value("${stats-server.url}")
    private String serverUrl;
    private static final String API_PREFIX = "/stats" + "?uris=";

    public List<HitDto> getStats(String uris) {
        System.out.println(serverUrl + API_PREFIX + uris);
        RestTemplate rest = new RestTemplate();
        HitDto[] forNow = rest.getForObject(serverUrl + API_PREFIX + uris, HitDto[].class);
        List<HitDto> hitDtos = Arrays.asList(forNow);
        return hitDtos;
    }
}
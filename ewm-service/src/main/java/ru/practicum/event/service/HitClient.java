package ru.practicum.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;


import java.util.List;
import java.util.Map;

@Service
public class HitClient extends BaseClient {

    private static final String API_PREFIX = "/hit";

    @Autowired
    public HitClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void createHit(String app, String uri) {
        Map<String, Object> parameters = Map.of(
                "app", app,
                "uri", uri
        );
        System.out.println(app);
        System.out.println(uri);
        post("?app={app}&uri={uri}", parameters);
    }
}

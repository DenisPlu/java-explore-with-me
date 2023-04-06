package ru.practicum.event.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HitsClient {

    @Value("${stats-server.url}")
    private String serverUrl;
    private static final String API_PREFIX = "/hit";

    public void createHit(String app, String uri) throws JSONException {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject hitJsonObject = new JSONObject();
        hitJsonObject.put("app", app);
        hitJsonObject.put("uri", uri);

        HttpEntity<String> request = new HttpEntity<String>(hitJsonObject.toString(), headers);
        rest.postForObject(serverUrl + API_PREFIX, request, String.class);
    }
}
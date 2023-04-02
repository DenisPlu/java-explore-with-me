package ru.practicum.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.hit.HitDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class HitsClient {

    private static final String API_path = "http://localhost:9090" + "/hit";

    //private final ObjectMapper objectMapper = new ObjectMapper();

    public void createHit(String app, String uri) throws JSONException, JsonProcessingException {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject hitJsonObject = new JSONObject();
        hitJsonObject.put("app", app);
        hitJsonObject.put("uri", uri);


        HttpEntity<String> request = new HttpEntity<String>(hitJsonObject.toString(), headers);
        String hitResultAsJsonStr = rest.postForObject(API_path, request, String.class);
        //JsonNode root = objectMapper.readTree(hitResultAsJsonStr);
    }
}
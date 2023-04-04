package ru.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.HitDto;
import ru.practicum.HitDtoMin;
import ru.practicum.hit.HitController;
import ru.practicum.hit.HitServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
public class HitControllerTest {
    @Mock
    private HitServiceImpl hitService;

    @InjectMocks
    private HitController hitController;
    private final ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();
    private MockMvc mvc;
    private HitDto hitDto1;
    private HitDto hitDto2;

    private HitDto hitDto3;

    private HitDtoMin hitDtoMin;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(hitController)
                .build();

        hitDto1 = new HitDto(
                "ewm-main-service",
                "/events/1",
                3
        );

        hitDto2 = new HitDto(
                "ewm-main-service",
                "/events/2",
                3
        );

        hitDto3 = new HitDto(
                "ewm-main-service",
                "/events/2",
                3
        );

        hitDtoMin = new HitDtoMin(
                3L,
                "ewm-main-service",
                "/events/2"
        );
    }

    @After("")
    public void reset_mocks() {
        Mockito.reset(hitService);
    }

    @Test
    void create() throws Exception {
        when(hitService.create(any()))
                .thenReturn("Информация сохранена");
        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(hitDtoMin))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", is("Информация сохранена")));
    }

    @Test
    void getStatsForUri() throws Exception {
        when(hitService.getStats(any(), any(), any(), any()))
                .thenReturn(List.of(hitDto1));
        mvc.perform(get("/stats/?start=2020-05-05 00:00:00&end=2035-05-05 00:00:00&uris={{/events/1}}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].app", is(hitDto1.getApp()), String.class))
                .andExpect(jsonPath("$.[0].uri", is(hitDto1.getUri()), String.class))
                .andExpect(jsonPath("$.[0].hits", is(hitDto1.getHits()), Long.class));
    }

    @Test
    void getStatsForAll() throws Exception {
        when(hitService.getStats(any(), any(), any(), any()))
                .thenReturn(List.of(hitDto1, hitDto2, hitDto3));
        mvc.perform(get("/stats/?start=2020-05-05 00:00:00&end=2035-05-05 00:00:00")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[1].app", is(hitDto2.getApp()), String.class))
                .andExpect(jsonPath("$.[1].uri", is(hitDto2.getUri()), String.class))
                .andExpect(jsonPath("$.[1].hits", is(hitDto2.getHits()), Long.class));
    }
}
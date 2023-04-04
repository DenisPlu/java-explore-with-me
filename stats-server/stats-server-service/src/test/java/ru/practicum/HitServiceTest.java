package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.hit.Hit;
import ru.practicum.hit.HitServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(SpringExtension.class)
public class HitServiceTest {

    private final EntityManager em;
    private final HitServiceImpl hitService;

    @BeforeEach
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void setUp() {
        hitService.create(new Hit(null, "ewm-main-service", "/events/1", "192.168.0.1", LocalDateTime.now()));
        hitService.create(new Hit(null, "ewm-main-service", "/events/2", "192.168.0.1", LocalDateTime.now()));
    }

    @Test
    @DirtiesContext
    void GetHitsByIp() {
        TypedQuery<Hit> query = em.createQuery("SELECT h FROM Hit h WHERE h.ip = :ip", Hit.class);
        List<Hit> hits = query.setParameter("ip", "192.168.0.1").getResultList();
        assertThat(hits.size(), equalTo(2));
        assertThat(hits.get(0).getApp(), equalTo("ewm-main-service"));
    }

    @Test
    @DirtiesContext
    void findHitsByUriAndTimeDiapason() {
        TypedQuery<Hit> query = em.createQuery(
                "SELECT h FROM Hit h WHERE h.uri = :uri AND h.timestamp >= :start AND h.timestamp <= :end", Hit.class);
        List<Hit> hits = query.setParameter("uri", List.of("/events/2"))
                .setParameter("start", LocalDateTime.now().minusHours(1))
                .setParameter("end", LocalDateTime.now().plusHours(1)).getResultList();
        assertThat(hits.size(), equalTo(1));
        assertThat(hits.get(0).getApp(), equalTo("ewm-main-service"));
    }
}
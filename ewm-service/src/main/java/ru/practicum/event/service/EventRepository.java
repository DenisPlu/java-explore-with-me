package ru.practicum.event.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.category.Category;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT * FROM events WHERE id IN (?1)", nativeQuery = true)
    List<Event> getByEventsIds(List<Long> eventsId);

    @Query(value = "SELECT * FROM events WHERE (UPPER(annotation) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(description) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "AND paid = ?2 AND event_date >= ?3 AND  event_date <= ?4 " +
            "AND category_id IN (?5) " +
            "ORDER BY ?6 DESC LIMIT ?7 OFFSET ?8", nativeQuery = true)
    List<Event> searchEventsPublic(String text, boolean paid, LocalDateTime startTime, LocalDateTime endTime,
                                                   List<Integer> categories, String sort,
                                                   Integer size, Integer from);

    @Query(value = "SELECT * FROM events WHERE (UPPER(annotation) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(description) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "AND paid = ?2 AND event_date >= ?3 AND  event_date <= ?4 " +
            "AND category_id IN (?5) AND confirmed_requests < participant_limit " +
            "ORDER BY ?6 DESC LIMIT ?7 OFFSET ?8", nativeQuery = true)
    List<Event> searchEventsPublicOnlyAvailable(String text, boolean paid, LocalDateTime startTime, LocalDateTime endTime,
                                                   List<Integer> categories, String sort,
                                                   Integer size, Integer from);

    @Query(value = "SELECT * FROM events WHERE initiator_id IN (?1) AND state IN (?2) AND category_id IN (?3) " +
            "AND event_date >= ?4 AND  event_date <= ?5 " +
            "ORDER BY id DESC LIMIT ?6 OFFSET ?7", nativeQuery = true)
    List<Event> searchEventsByAdmin(List<Long> usersId, List<String> states, List<Integer> categories,
                                    LocalDateTime startTime, LocalDateTime endTime,
                                    Integer size, Integer from);

    @Query(value = "SELECT * FROM events WHERE id = ?1 AND state = 'PUBLISHED'", nativeQuery = true)
    Event getByIdIfPublished(Long eventsId);

    @Query(value = "SELECT * FROM events WHERE initiator_id = ?1 ORDER BY id DESC LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<Event> getByUserIdWithPagination(Long id, Integer size, Integer from);

    @Query(value = "SELECT * FROM events WHERE initiator_id = ?1 AND id = ?2 ORDER BY id DESC LIMIT ?3 OFFSET ?4", nativeQuery = true)
    Event getByUserAndEventId(Long userId, Long eventId, Integer size, Integer from);
}

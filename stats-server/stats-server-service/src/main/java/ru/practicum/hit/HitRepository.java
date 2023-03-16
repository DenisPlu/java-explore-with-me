package ru.practicum.hit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT h FROM Hit h WHERE h.uri IN (?1) AND h.timestamp >= ?2 AND h.timestamp <= ?3")
    List<Hit> findHitsByUriAndTimeDiapason(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT * FROM hits WHERE (created >= ? AND created <= ?)", nativeQuery = true)
    List<Hit> findHitsByTimeDiapason(LocalDateTime start, LocalDateTime end);
}
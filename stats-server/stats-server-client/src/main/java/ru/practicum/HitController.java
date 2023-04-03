package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.hit.HitMapper;
import ru.practicum.hit.HitServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class HitController {

    private final HitServiceImpl hitService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public String create(@RequestBody HitDtoMin hitDtoMin,
                         HttpServletRequest request) {
        String clientIP = request.getRemoteAddr();
        log.info("Received a request to create a new Hit, hitDtoMin = {}, clientIP = {}", hitDtoMin, clientIP);
        return hitService.create(HitMapper.toHitFromHitDtoMin(hitDtoMin, clientIP));
    }

    @GetMapping("/stats")
    public List<HitDto> getStats(@RequestParam(defaultValue = "2000-01-01 00:00:00") String start,
                                 @RequestParam(defaultValue = "2030-01-01 00:00:00") String end,
                                 @RequestParam(defaultValue = "all") List<String> uris,
                                 @RequestParam(defaultValue = "false") String unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startTime = LocalDateTime.parse(start.replaceAll(" ", "T"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(end.replaceAll(" ", "T"), formatter);
        return hitService.getStats(startTime, endTime, uris, unique);
    }
}
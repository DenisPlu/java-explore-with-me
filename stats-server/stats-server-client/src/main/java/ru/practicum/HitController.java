package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.hit.HitServiceImpl;

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
    public String create(@RequestBody HitStringDateDto hitDto) {
        log.info("Received a request to create a new Hit: {}", hitDto);
        return hitService.create(hitDto);
    }

    @GetMapping("/stats")
    public List<HitDto> getStats(@RequestParam String start,
                                 @RequestParam String end,
                                 @RequestParam(defaultValue = "all") List<String> uris,
                                 @RequestParam(defaultValue = "false") String unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startTime = LocalDateTime.parse(start.replaceAll(" ", "T"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(end.replaceAll(" ", "T"), formatter);
        return hitService.getStats(startTime, endTime, uris, unique);
    }
}
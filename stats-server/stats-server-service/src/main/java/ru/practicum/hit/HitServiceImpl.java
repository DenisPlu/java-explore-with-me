package ru.practicum.hit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static ru.practicum.hit.HitMapper.toHitDto;

@Service
@Getter
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {

    private final HitRepository hitRepository;

    @Override
    public String create(Hit hit) {

        System.out.println(hitRepository.save(hit));
        return "Информация сохранена";
    }

    @Override
    public List<HitDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, String unique) {
        List<Hit> baseHitList;
        if (uris.get(0).equals("all")) {
            baseHitList = hitRepository.findHitsByTimeDiapason(start, end);
        } else {
            System.out.println(uris);
            baseHitList = hitRepository.findHitsByUriAndTimeDiapason(uris, start, end);
        }
        if (Boolean.parseBoolean(unique)) {
            return toUniqueHitsDtoList(baseHitList);
        } else {
            return toHitsDtoList(baseHitList);
        }
    }

    private List<HitDto> toUniqueHitsDtoList(List<Hit> hits) {
        Map<List<Object>, Hit> uniqueHitsMap = new HashMap<>();
        for (Hit hit : hits) {
            uniqueHitsMap.put(Arrays.asList(hit.getApp(), hit.getUri(), hit.getId()), hit);
        }
        return getHitDtoWithFrequencyFromMap(hits, uniqueHitsMap);
    }

    private List<HitDto> toHitsDtoList(List<Hit> hits) {
        Map<List<Object>, Hit> uniqueHitsMap = new HashMap<>();
        for (Hit hit : hits) {
            uniqueHitsMap.put(Arrays.asList(hit.getApp(), hit.getUri()), hit);
        }
        return getHitDtoWithFrequencyFromMap(hits, uniqueHitsMap);
    }

    private List<HitDto> getHitDtoWithFrequencyFromMap(List<Hit> hits, Map<List<Object>, Hit> uniqueHitsMap) {
        Collection<Hit> uniqueList = uniqueHitsMap.values();
        Map<String, Long> frequency = hits.stream().collect(groupingBy(Hit::getUri, Collectors.counting()));
        List<HitDto> hitsDtoList = new ArrayList<>();
        for (Hit hit : uniqueList) {
            hitsDtoList.add(toHitDto(hit, frequency.get(hit.getUri())));
        }
        hitsDtoList.sort(Comparator.comparingLong(HitDto::getHits).reversed());
        return hitsDtoList;
    }
}
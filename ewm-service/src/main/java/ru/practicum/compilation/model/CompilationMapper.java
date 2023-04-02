package ru.practicum.compilation.model;

import ru.practicum.event.dto.EventShortDto;

import java.util.List;

public class CompilationMapper {

    public CompilationMapper() {
    }

    public static CompilationFullDto toCompilationFullDtoFromCompilation(Compilation compilation, List<EventShortDto> events) {
        return new CompilationFullDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned(),
                events
        );
    }

    public static Compilation toCompilationFromCompilationNewDto(CompilationNewDto compilationNewDto) {
        return new Compilation(
                compilationNewDto.getId(),
                compilationNewDto.getTitle(),
                compilationNewDto.getPinned()
        );
    }
}

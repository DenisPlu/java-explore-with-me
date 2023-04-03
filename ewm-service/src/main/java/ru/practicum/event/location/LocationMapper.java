package ru.practicum.event.location;

public class LocationMapper {

    public LocationMapper() {
    }

    public static LocationDto toLocationDtoFromLocation(Location location) {
        return new LocationDto(
                location.getLat(),
                location.getLon()
        );
    }
}
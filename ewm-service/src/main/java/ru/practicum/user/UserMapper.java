package ru.practicum.user;

public class UserMapper {

    public UserMapper() {}

    public static UserShortDto toUserShortDtoFromUser(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }
}

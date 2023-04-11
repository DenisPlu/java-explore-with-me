package ru.practicum.user;

import java.util.List;

public interface UserService {

    User create(User user);

    List<User> getByIds(List<Long> ids, Integer size, Integer from);

    User get(Long id);

    User update(Long id, User user);

    void delete(Long id);
}
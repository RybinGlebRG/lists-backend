package ru.rerumu.lists.model.user;

import ru.rerumu.lists.dao.user.UserDtoDao;

import java.util.List;

public interface UserFactory {

    User findById(Long userId);
    List<User> findByIds(List<Long> userIds);
    User fromDTO(UserDtoDao userDtoDao);
}

package ru.rerumu.lists.model.user;

import java.util.List;

public interface UserFactory {

    User findById(Long userId);
    List<User> findByIds(List<Long> userIds);
}

package ru.rerumu.lists.domain.user;

import ru.rerumu.lists.dao.user.UserDtoDao;

public interface UserFactory {

    User fromDTO(UserDtoDao userDtoDao);

    User build(
            Long userId,
            String name,
            char[] password
    );
}

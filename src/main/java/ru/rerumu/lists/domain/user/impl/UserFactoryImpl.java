package ru.rerumu.lists.domain.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.user.UserDtoDao;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserFactory;

import java.util.Arrays;

@Component
public class UserFactoryImpl implements UserFactory {


    @Autowired
    public UserFactoryImpl() {
    }

    @Override
    public User fromDTO(UserDtoDao userDtoDao) {
        return new UserImpl(
                userDtoDao.getUserId(),
                userDtoDao.getName(),
                userDtoDao.getPassword()
        );
    }

    @Override
    public User build(Long userId, String name, char[] password) {
        return new UserImpl(userId, name, Arrays.toString(password));
    }
}

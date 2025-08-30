package ru.rerumu.lists.domain.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.user.UserDtoDao;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserFactory;
import ru.rerumu.lists.dao.user.UsersRepository;

import java.util.List;

@Component
public class UserFactoryImpl implements UserFactory {

    private final UsersRepository usersRepository;

    @Autowired
    public UserFactoryImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public User findById(Long userId) {
        User user = usersRepository.findById(userId);

        if (user == null) {
            throw new EntityNotFoundException();
        }

        return user;
    }

    @Override
    public List<User> findByIds(List<Long> userIds) {
        return usersRepository.findByIds(userIds);
    }

    @Override
    public User fromDTO(UserDtoDao userDtoDao) {
        return new User(
                userDtoDao.getUserId(),
                userDtoDao.getName(),
                userDtoDao.getPassword()
        );
    }
}

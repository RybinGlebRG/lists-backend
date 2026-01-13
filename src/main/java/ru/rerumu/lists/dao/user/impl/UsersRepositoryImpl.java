package ru.rerumu.lists.dao.user.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.base.impl.CrudRepositoryEntityImpl;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.dao.user.mapper.UserMapper;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserFactory;

import java.util.List;

@Component
public class UsersRepositoryImpl extends CrudRepositoryEntityImpl<User,Long>  implements UsersRepository {


    private final UserMapper userMapper;
    private final UserFactory userFactory;

    @Autowired
    public UsersRepositoryImpl(UserMapper userMapper, UserFactory userFactory) {
        super(userMapper);
        this.userMapper = userMapper;
        this.userFactory = userFactory;
    }

    @Override
    public User getOne(String name) {
        User user = userMapper.getOne(name);
        return user;
    }

    @Override
    public boolean isOwner(String name, Long listId) {
        int count = userMapper.count(name, listId);
        return count > 0;
    }

    @Override
    public boolean isOwnerAuthor(String name, Long authorId) {
        int count = userMapper.countAuthor(name, authorId);
        return count > 0;
    }

    @Override
    public boolean isOwnerBook(String name, Long bookId) {
        return userMapper.countBooks(name, bookId) > 0;
    }

    @Override
    public boolean isOwnerSeries(String name, Long seriesId) {
        return userMapper.countSeries(name, seriesId) > 0;
    }

    @Override
    public User findById(Long userId) {
        User user =  userMapper.findById(userId);
        if (user == null) {
            throw new EntityNotFoundException();
        }
        return user;
    }

    // TODO: fix null
    @Override
    public List<User> findByIds(List<Long> userIds) {
        return userMapper.findByIds(userIds, null);
    }

    @Override
    public @NonNull User create(@NonNull Long userId, @NonNull String name, char[] plainPassword) {
        User user = userFactory.build(userId, name, plainPassword);
        userMapper.create(user);
        return user;
    }
}

package ru.rerumu.lists.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.mappers.UserMapper;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.TokenRequest;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.repository.UsersRepository;

@Component
public class UsersRepositoryImpl implements UsersRepository {

    @Autowired
    private UserMapper userMapper;

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
}

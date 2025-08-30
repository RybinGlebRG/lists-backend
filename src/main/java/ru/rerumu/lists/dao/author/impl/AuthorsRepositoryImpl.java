package ru.rerumu.lists.dao.author.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.author.AuthorDtoDao;
import ru.rerumu.lists.dao.author.AuthorsRepository;
import ru.rerumu.lists.dao.author.mapper.AuthorMapper;
import ru.rerumu.lists.domain.user.User;

import java.util.List;

// TODO: fix null
@Component
public class AuthorsRepositoryImpl implements AuthorsRepository {

    @Autowired
    private AuthorMapper authorMapper;

    @Override
    public AuthorDtoDao getOne(Long authorId) {
        AuthorDtoDao dto = authorMapper.findById(authorId, null);
        if (dto == null) {
            throw new EntityNotFoundException();
        }
        return dto;
    }

    @Override
    public List<AuthorDtoDao> getAll(User user) {
        return authorMapper.findByUser(user);
    }

    @Override
    public void addOne(AuthorDtoDao author) {
        authorMapper.create(author);
    }

    @Override
    public Long getNextId() {
        return authorMapper.getNextId();
    }

    @Override
    public void deleteOne(Long authorId) {
        authorMapper.delete(authorId, null);
    }

    @Override
    public void addToBook(Long authorId, Long bookId) {

    }

    @Override
    public List<AuthorDtoDao> findByIds(List<Long> ids) {
        return authorMapper.findByIds(ids, null);
    }
}

package ru.rerumu.lists.dao.author.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.author.AuthorDtoDao;
import ru.rerumu.lists.dao.author.AuthorsRepository;
import ru.rerumu.lists.dao.author.mapper.AuthorMapper;
import ru.rerumu.lists.model.author.impl.AuthorImpl;
import ru.rerumu.lists.model.user.User;

import java.util.List;
import java.util.Optional;

@Component
public class AuthorsRepositoryImpl implements AuthorsRepository {

    @Autowired
    private AuthorMapper authorMapper;

    @Override
    public Optional<AuthorImpl> getOne(Long readListId, Long authorId) {
        AuthorImpl author = authorMapper.getOne(readListId, authorId);
        if (author != null){
            return Optional.of(author);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public AuthorDtoDao getOne(Long authorId) {
        AuthorDtoDao dto = authorMapper.findById(authorId);
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
        authorMapper.delete(authorId);
    }
}

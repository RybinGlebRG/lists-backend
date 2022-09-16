package ru.rerumu.lists.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.mappers.AuthorMapper;
import ru.rerumu.lists.mappers.SeriesMapper;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.repository.AuthorsRepository;
import ru.rerumu.lists.repository.SeriesRepository;

import java.util.List;

@Component
public class AuthorsRepositoryImpl implements AuthorsRepository {

    @Autowired
    private AuthorMapper authorMapper;

    @Override
    public Author getOne(Long readListId, Long authorId) {

        return authorMapper.getOne(readListId, authorId);
    }

    @Override
    public List<Author> getAll(Long readListId) {
        return authorMapper.getAll(readListId);
    }

    @Override
    public void addOne(Author author) {
        authorMapper.addOne(author.getReadListId(), author.getAuthorId(), author.getName());
    }

    @Override
    public Long getNextId() {
        return authorMapper.getNextId();
    }

    @Override
    public void deleteOne(Long authorId) {
        authorMapper.deleteOne(authorId);
    }
}

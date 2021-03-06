package ru.rerumu.lists.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.mappers.AuthorMapper;
import ru.rerumu.lists.mappers.SeriesMapper;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.repository.AuthorsRepository;
import ru.rerumu.lists.repository.SeriesRepository;

@Component
public class AuthorsRepositoryImpl implements AuthorsRepository {

    @Autowired
    private AuthorMapper authorMapper;

    @Override
    public Author getOne(Long readListId, Long authorId) {

        return authorMapper.getOne(readListId, authorId);
    }
}

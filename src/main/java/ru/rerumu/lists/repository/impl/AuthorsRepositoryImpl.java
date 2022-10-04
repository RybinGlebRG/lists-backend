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
import java.util.Optional;

@Component
public class AuthorsRepositoryImpl implements AuthorsRepository {

    @Autowired
    private AuthorMapper authorMapper;

    @Override
    public Optional<Author> getOne(Long readListId, Long authorId) {
        Author author = authorMapper.getOne(readListId, authorId);
        if (author != null){
            return Optional.of(author);
        } else {
            return Optional.empty();
        }
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

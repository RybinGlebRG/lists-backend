package ru.rerumu.lists.repository.impl;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.mappers.AuthorBookMapper;
import ru.rerumu.lists.repository.AuthorsBooksRepository;

@Component
public class AuthorsBooksRepositoryImpl implements AuthorsBooksRepository {

    private final AuthorBookMapper authorBookMapper;

    public AuthorsBooksRepositoryImpl(AuthorBookMapper authorBookMapper){
        this.authorBookMapper = authorBookMapper;
    }
    @Override
    public void deleteByAuthor(Long authorId) {
        authorBookMapper.deleteByAuthor(authorId);
    }

    @Override
    public void add(Long bookId, Long authorId, Long readListId) {
        authorBookMapper.add(bookId, authorId, readListId);
    }
}

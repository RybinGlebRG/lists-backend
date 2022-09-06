package ru.rerumu.lists.repository.impl;

import ru.rerumu.lists.repository.AuthorsBooksRepository;

import javax.naming.OperationNotSupportedException;

public class AuthorsBooksRepositoryImpl implements AuthorsBooksRepository {
    @Override
    public void deleteByAuthor(Long authorId) {
        throw new UnsupportedOperationException();
        // TODO: write
    }
}

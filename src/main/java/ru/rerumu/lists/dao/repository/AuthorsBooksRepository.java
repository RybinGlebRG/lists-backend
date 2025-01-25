package ru.rerumu.lists.dao.repository;

import ru.rerumu.lists.model.AuthorBookRelation;

import java.util.List;

public interface AuthorsBooksRepository {

    void deleteByAuthor(Long authorId);

    void add(Long bookId, Long authorId, Long readListId);

    List<AuthorBookRelation> getByBookId(Long bookId, Long readListId);

    void delete(long bookId, long authorId, long readListId);
}

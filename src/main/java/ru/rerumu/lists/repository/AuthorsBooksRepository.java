package ru.rerumu.lists.repository;

public interface AuthorsBooksRepository {

    void deleteByAuthor(Long authorId);

    void add(Long bookId, Long authorId, Long readListId);
}

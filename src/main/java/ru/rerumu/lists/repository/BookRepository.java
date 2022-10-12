package ru.rerumu.lists.repository;

import ru.rerumu.lists.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    void update(Book book);

    @Deprecated
    Book getOne(Long readListId, Long bookId);
    Optional<Book> getOne(Long bookId);
    List<Book> getAll(Long readListId);

    Long getNextId();

    void addOne(Book book);

    void delete(Long bookId);
}

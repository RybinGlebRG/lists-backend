package ru.rerumu.lists.repository;

import ru.rerumu.lists.model.book.BookImpl;
import ru.rerumu.lists.model.User;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    void update(BookImpl book);

    @Deprecated
    BookImpl getOne(Long readListId, Long bookId);
    Optional<BookImpl> getOne(Long bookId);
    List<BookImpl> getAll(Long readListId);
    List<BookImpl> getAllChained(Long readListId);

    Long getNextId();

    void addOne(BookImpl book);

    void delete(Long bookId);

    Optional<User> getBookUser(Long bookId);
}

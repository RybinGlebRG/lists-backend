package ru.rerumu.lists.repository;

import ru.rerumu.lists.model.book.Book;
import ru.rerumu.lists.model.book.BookImpl;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.model.book.BookDTO;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    void update(BookImpl book);

    Optional<BookDTO> getOneDTO(Long bookId);
    List<BookDTO> getAll(Long readListId);
    List<BookDTO> getAllChained(Long readListId);

    Long getNextId();

    void addOne(BookImpl book);

    void delete(Long bookId);

    Optional<User> getBookUser(Long bookId);
}

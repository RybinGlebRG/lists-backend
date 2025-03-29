package ru.rerumu.lists.dao.book;

import ru.rerumu.lists.model.book.impl.BookImpl;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.book.BookDTO;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    void update(BookImpl book);

    Optional<BookDTO> getOneDTO(Long bookId);
    List<BookDTO> getAll(Long readListId);
    List<BookDTO> getAllChained(Long readListId);

    BookDtoDao findById(Long id);

    Long getNextId();

    void addOne(BookImpl book);

    void delete(Long bookId);

    Optional<User> getBookUser(Long bookId);
}

package ru.rerumu.lists.dao.book;

import ru.rerumu.lists.model.book.impl.BookImpl;
import ru.rerumu.lists.model.user.User;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    void update(BookImpl book);

    BookDtoDao findById(Long id);
    List<BookDtoDao> findByUser(User user);
    List<BookDtoDao> findByUserChained(Long userId);

    Long getNextId();

    void addOne(BookImpl book);

    void delete(Long bookId);

    Optional<User> getBookUser(Long bookId);
}

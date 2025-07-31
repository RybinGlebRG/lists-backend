package ru.rerumu.lists.dao.book;

import lombok.NonNull;
import ru.rerumu.lists.domain.book.impl.BookImpl;
import ru.rerumu.lists.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    void update(BookImpl book);

    /**
     * Find book by id
     */
    @NonNull
    BookDtoDao findById(Long id, Long userId);

    List<BookDtoDao> findByUser(User user);
    List<BookDtoDao> findByUserChained(User user);

    Long getNextId();

    void addOne(BookImpl book);

    void delete(Long bookId);

    Optional<User> getBookUser(Long bookId);
}

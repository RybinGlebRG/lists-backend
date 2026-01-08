package ru.rerumu.lists.dao.book;

import lombok.NonNull;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    void update(Book book);

    /**
     * Find book by id
     */
    @NonNull
    Book findById(Long id, Long userId);

    List<Book> findByUser(User user);
    List<Book> findByUserChained(User user);

    Long getNextId();

    void addOne(Book book);

    void delete(Long bookId, User user);

    Optional<User> getBookUser(Long bookId);

    void save(Book book);

    /**
     * Delete book
     * @param book Book to delete
     */
    void delete(Book book);
}

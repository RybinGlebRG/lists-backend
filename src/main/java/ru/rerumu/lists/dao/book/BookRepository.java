package ru.rerumu.lists.dao.book;

import lombok.NonNull;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.booktype.BookType;
import ru.rerumu.lists.domain.readingrecordstatus.ReadingRecordStatuses;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

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

    void save(Book book);

    /**
     * Delete book
     * @param book Book to delete
     */
    void delete(Book book);

    /**
     * Create book
     */
    Book create(
            String title,
            Integer lastChapter,
            String note,
            ReadingRecordStatuses bookStatus,
            LocalDateTime insertDate,
            BookType bookType,
            String URL,
            User user
    );

    @NonNull
    Book attach(@NonNull BookMyBatisEntity bookMyBatisEntity);
}

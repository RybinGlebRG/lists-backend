package ru.rerumu.lists.dao.book.impl;

import com.jcabi.aspects.Loggable;
import lombok.extern.slf4j.Slf4j;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.book.BookDtoDao;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.dao.book.mapper.BookMapper;
import ru.rerumu.lists.model.book.BookDTO;
import ru.rerumu.lists.model.book.impl.BookImpl;
import ru.rerumu.lists.model.user.User;

import java.util.List;
import java.util.Optional;

// TODO: Refactor class
@Slf4j
public class BookRepositoryImpl implements BookRepository {

    private final BookMapper bookMapper;

    public BookRepositoryImpl(
            BookMapper bookMapper
    ) {
        this.bookMapper = bookMapper;
    }


    @Override
    public void update(BookImpl book) {
        BookDTO bookDTO = book.toDTO();

        bookMapper.update(
                book.getReadListId(),
                book.getBookId(),
                book.getTitle(),
                book.getBookStatus().statusId(),
                book.getInsertDate(),
                book.getLastUpdateDate(),
                book.getLastChapter().isPresent() ? book.getLastChapter().get() : null,
                book.getBookType() != null ? book.getBookType().getId() : null,
                book.getNote(),
                bookDTO.URL
        );
    }

    @Override
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    public List<BookDtoDao> findByUserChained(Long userId) {
        List<BookDtoDao> bookDtoList = bookMapper.findByUserChained(userId);
        return bookDtoList;
    }

    @Override
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    public BookDtoDao findById(Long id) {
        BookDtoDao book = bookMapper.findById(id);

        if (book == null) {
            throw new EntityNotFoundException();
        }

        return book;
    }

    @Override
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    public List<BookDtoDao> findByUser(User user) {
        return bookMapper.findByUser(user);
    }

    @Override
    public Long getNextId() {
        return bookMapper.getNextId();
    }

    @Override
    public void addOne(BookImpl book) {
        BookDTO bookDTO = book.toDTO();

        bookMapper.addOne(
                book.getBookId(),
                book.getReadListId(),
                book.getTitle(),
                book.getBookStatus().statusId(),
                book.getInsertDate(),
                book.getLastUpdateDate(),
                book.getLastChapter().isPresent() ? book.getLastChapter().get() : null,
                book.getBookType() != null ? book.getBookType().getId() : null,
                book.getNote(),
                bookDTO.URL,
                bookDTO.userId
        );
    }

    @Override
    public void delete(Long bookId) {
        bookMapper.delete(bookId);
    }

    @Override
    public Optional<User> getBookUser(Long bookId) {
        return Optional.ofNullable(bookMapper.getBookUser(bookId));
    }
}

package ru.rerumu.lists.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.mappers.BookMapper;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Component
public class BookRepositoryImpl implements BookRepository {

    @Autowired
    private BookMapper bookMapper;


    @Override
    public void update(Book book) {
        bookMapper.update(
                book.getReadListId(),
                book.getBookId(),
                book.getTitle(),
                book.getBookStatus().getId(),
                book.getInsertDate(),
                book.getLastUpdateDate(),
                book.getLastChapter()
        );
//        bookMapper.updateAuthor(
//                book.getReadListId(),
//                book.getBookId(),
//                book.getReadListId(),
//                book.getAuthorId()
//        );
//        return getOne(book.getReadListId(), book.getBookId());
    }

    @Override
    public Book getOne(Long readListId, Long bookId) {
        return bookMapper.getOne(bookId);
    }

    @Override
    public Optional<Book> getOne(Long bookId) {
        Book book = bookMapper.getOne( bookId);
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> getAll(Long readListId) {
        return bookMapper.getAll(readListId);
    }

    @Override
    public Long getNextId() {
        return bookMapper.getNextId();
    }

    @Override
    public void addOne(Book book) {
        bookMapper.addOne(
                book.getBookId(),
                book.getReadListId(),
                book.getTitle(),
                book.getBookStatus().getId(),
                book.getInsertDate(),
                book.getLastUpdateDate(),
                book.getLastChapter()
        );
    }

    @Override
    public void delete(Long bookId) {
        bookMapper.delete(bookId);
    }
}

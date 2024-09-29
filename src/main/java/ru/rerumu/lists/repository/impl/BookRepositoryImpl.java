package ru.rerumu.lists.repository.impl;

import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.mappers.BookMapper;
import ru.rerumu.lists.mappers.ReadingRecordMapper;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.BookStatus;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;
import ru.rerumu.lists.model.dto.BookDTO;
import ru.rerumu.lists.repository.BookRepository;
import ru.rerumu.lists.repository.CrudRepository;

import java.util.*;
import java.util.stream.Collectors;

// TODO: Refactor class
public class BookRepositoryImpl implements BookRepository {

    private final BookMapper bookMapper;
    private final ReadingRecordMapper readingRecordMapper;

    public BookRepositoryImpl(
            BookMapper bookMapper, ReadingRecordMapper readingRecordMapper
    ) {
        this.bookMapper = bookMapper;
        this.readingRecordMapper = readingRecordMapper;
    }


    @Override
    public void update(Book book) {
        bookMapper.update(
                book.getReadListId(),
                book.getBookId(),
                book.getTitle(),
                book.getBookStatus().statusId(),
                book.getInsertDate(),
                book.getLastUpdateDate(),
                book.getLastChapter().isPresent() ? book.getLastChapter().get() : null,
                book.getBookType() != null ? book.getBookType().getId() : null,
                book.note()
        );
    }

    @Deprecated
    @Override
    public Book getOne(Long readListId, Long bookId) {
        Optional<Book> optionalBook = getOne(bookId);
        if (optionalBook.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return optionalBook.get();
    }

    @Override
    public Optional<Book> getOne(Long bookId) {
        BookDTO bookDTO = bookMapper.getOne(bookId);
        if (bookDTO == null) {
            return Optional.empty();
        } else {
            List<ReadingRecord> readingRecord = readingRecordMapper.findByBookId(bookDTO.bookId);
            bookDTO = bookDTO.toBuilder()
                    .readingRecords(readingRecord)
                    .build();
            return Optional.of(bookDTO.toDomain());
        }
    }

    @Override
    public List<Book> getAll(Long readListId) {
        List<BookDTO> bookDTOList = bookMapper.getAll(readListId);

        HashMap<Long, List<ReadingRecord>> bookToRecordMap = bookDTOList.stream()
                .flatMap(bookDTO -> readingRecordMapper.findByBookId(bookDTO.bookId).stream())
                .collect(Collectors.groupingBy(
                        ReadingRecord::bookId,
                        HashMap::new,
                        Collectors.toCollection(ArrayList::new)
                ));

        List<Book> bookList = bookDTOList.stream()
                .map(bookDTO -> bookDTO.toBuilder()
                        .readingRecords(bookToRecordMap.get(bookDTO.bookId))
                        .build())
                .map(BookDTO::toDomain)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));

        return bookList;
    }

    @Override
    public List<Book> getAllChained(Long readListId) {
        List<BookDTO> bookDTOList = bookMapper.getAllChained(readListId);

        List<Book> bookList = bookDTOList.stream()
                .map(BookDTO::toDomain)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));

        return bookList;
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
                book.getBookStatus().statusId(),
                book.getInsertDate(),
                book.getLastUpdateDate(),
                book.getLastChapter().isPresent() ? book.getLastChapter().get() : null,
                book.getBookType() != null ? book.getBookType().getId() : null,
                book.note()
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

package ru.rerumu.lists.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.rerumu.lists.mappers.BookMapper;
import ru.rerumu.lists.mappers.ReadingRecordMapper;
import ru.rerumu.lists.model.book.BookImpl;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;
import ru.rerumu.lists.model.dto.BookDTO;
import ru.rerumu.lists.model.dto.BookOrderedDTO;
import ru.rerumu.lists.repository.BookRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: Refactor class
@Slf4j
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
    public void update(BookImpl book) {
        bookMapper.update(
                book.getReadListId(),
                book.getBookId(),
                book.getTitle(),
                book.getBookStatus().statusId(),
                book.getInsertDate(),
                book.getLastUpdateDate(),
                book.getLastChapter().isPresent() ? book.getLastChapter().get() : null,
                book.getBookType() != null ? book.getBookType().getId() : null,
                book.getNote()
        );
    }

    @Deprecated
    @Override
    public BookImpl getOne(Long readListId, Long bookId) {
        Optional<BookImpl> optionalBook = getOne(bookId);
        if (optionalBook.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return optionalBook.get();
    }

    @Override
    public Optional<BookImpl> getOne(Long bookId) {
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
    public List<BookImpl> getAll(Long readListId) {
        List<BookDTO> bookDTOList = bookMapper.getAll(readListId);

        HashMap<Long, List<ReadingRecord>> bookToRecordMap = bookDTOList.stream()
                .flatMap(bookDTO -> readingRecordMapper.findByBookId(bookDTO.bookId).stream())
                .collect(Collectors.groupingBy(
                        ReadingRecord::bookId,
                        HashMap::new,
                        Collectors.toCollection(ArrayList::new)
                ));

        List<BookImpl> bookList = bookDTOList.stream()
                .map(bookDTO -> bookDTO.toBuilder()
                        .readingRecords(bookToRecordMap.get(bookDTO.bookId))
                        .build())
                .map(BookDTO::toDomain)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));

        return bookList;
    }

    @Override
    public List<BookImpl> getAllChained(Long readListId) {
        List<BookDTO> bookDTOList = bookMapper.getAllChained(readListId);

        List<Long> bookIds = bookDTOList.stream()
                .map(BookDTO::getBookId)
                .collect(Collectors.toCollection(ArrayList::new));

        bookIds.addAll(
                bookDTOList.stream()
                .flatMap(bookDTO -> {
                    if (bookDTO.previousBooks != null){
                        return  bookDTO.previousBooks.stream();
                    } else {
                        return Stream.empty();
                    }
                })
                .map(bookOrderedDTO -> bookOrderedDTO.getBookDTO().bookId)
                .collect(Collectors.toCollection(ArrayList::new))
        );

        List<ReadingRecord> readingRecords = readingRecordMapper.findByBookIds(bookIds);

        Map<Long, List<ReadingRecord>> bookId2ReadingRecordMap = readingRecords.stream()
                .collect(Collectors.groupingBy(
                        ReadingRecord::bookId,
                        HashMap::new,
                        Collectors.toCollection(ArrayList::new)
                ));

        for(BookDTO bookDTO: bookDTOList){
            List<ReadingRecord> records = bookId2ReadingRecordMap.get(bookDTO.getBookId());

            if (records == null){
                records = new ArrayList<>();
            }

            bookDTO.setReadingRecords(records);

            for (BookOrderedDTO bookOrderedDTO: bookDTO.getPreviousBooks()){
                List<ReadingRecord> recordsOrdered = bookId2ReadingRecordMap.get(bookOrderedDTO.getBookDTO().getBookId());
                if (recordsOrdered == null){
                    recordsOrdered = new ArrayList<>();
                }
                bookOrderedDTO.getBookDTO().setReadingRecords(recordsOrdered);
            }
        }

        List<BookImpl> bookList = bookDTOList.stream()
                .peek(bookDTO -> log.debug(bookDTO.toString()))
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
    public void addOne(BookImpl book) {
        bookMapper.addOne(
                book.getBookId(),
                book.getReadListId(),
                book.getTitle(),
                book.getBookStatus().statusId(),
                book.getInsertDate(),
                book.getLastUpdateDate(),
                book.getLastChapter().isPresent() ? book.getLastChapter().get() : null,
                book.getBookType() != null ? book.getBookType().getId() : null,
                book.getNote()
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

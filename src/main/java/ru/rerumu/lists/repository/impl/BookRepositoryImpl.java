package ru.rerumu.lists.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.rerumu.lists.mappers.BookMapper;
import ru.rerumu.lists.mappers.ReadingRecordMapper;
import ru.rerumu.lists.model.book.BookFactory;
import ru.rerumu.lists.model.book.BookImpl;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.model.book.reading_records.ReadingRecordImpl;
import ru.rerumu.lists.model.book.BookDTO;
import ru.rerumu.lists.model.dto.BookOrderedDTO;
import ru.rerumu.lists.repository.BookRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Optional<BookDTO> getOneDTO(Long bookId) {
        return Optional.ofNullable(bookMapper.getOne(bookId));
    }

    @Override
    public List<BookDTO> getAll(Long readListId) {
        return bookMapper.getAll(readListId);
    }

    @Override
    public List<BookDTO> getAllChained(Long readListId) {
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

//        List<ReadingRecordImpl> readingRecords = readingRecordMapper.findByBookIds(bookIds);
//
//        Map<Long, List<ReadingRecordImpl>> bookId2ReadingRecordMap = readingRecords.stream()
//                .collect(Collectors.groupingBy(
//                        ReadingRecordImpl::getBookId,
//                        HashMap::new,
//                        Collectors.toCollection(ArrayList::new)
//                ));

//        for(BookDTO bookDTO: bookDTOList){
////            List<ReadingRecordImpl> records = bookId2ReadingRecordMap.get(bookDTO.getBookId());
////
////            if (records == null){
////                records = new ArrayList<>();
////            }
////
////            bookDTO.setReadingRecords(records);
//
////            for (BookOrderedDTO bookOrderedDTO: bookDTO.getPreviousBooks()){
////                List<ReadingRecordImpl> recordsOrdered = bookId2ReadingRecordMap.get(bookOrderedDTO.getBookDTO().getBookId());
////                if (recordsOrdered == null){
////                    recordsOrdered = new ArrayList<>();
////                }
////                bookOrderedDTO.getBookDTO().setReadingRecords(recordsOrdered);
////            }
//        }

        List<BookDTO> bookList = bookDTOList.stream()
                .peek(bookDTO -> log.debug(bookDTO.toString()))
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
                bookDTO.URL
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

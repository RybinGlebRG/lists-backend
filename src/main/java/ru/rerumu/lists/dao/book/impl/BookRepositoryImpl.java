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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
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
    public BookDtoDao findById(Long id) {
        BookDtoDao book = bookMapper.findById(id);

        if (book == null) {
            throw new EntityNotFoundException();
        }

        return book;
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

package ru.rerumu.lists.model.book;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.BookChain;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.book.reading_records.ReadingRecord;
import ru.rerumu.lists.model.book.type.BookType;
import ru.rerumu.lists.model.book.reading_records.ReadingRecordFactory;
import ru.rerumu.lists.model.book.type.BookTypeFactory;
import ru.rerumu.lists.model.dto.BookOrderedDTO;
import ru.rerumu.lists.repository.BookRepository;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BookFactory {

    private final DateFactory dateFactory;
    private final BookRepository bookRepository;
    private final ReadingRecordFactory readingRecordFactory;
    private final BookTypeFactory bookTypeFactory;

    @Autowired
    public BookFactory(DateFactory dateFactory, BookRepository bookRepository, ReadingRecordFactory readingRecordFactory, BookTypeFactory bookTypeFactory) {
        this.dateFactory = dateFactory;
        this.bookRepository = bookRepository;
        this.readingRecordFactory = readingRecordFactory;
        this.bookTypeFactory = bookTypeFactory;
    }

    public Book createBook(
            Long readListId,
            String title,
            Integer lastChapter,
            String note,
            BookStatusRecord bookStatus,
            LocalDateTime insertDate,
            BookType bookType,
            String URL
    ) throws EmptyMandatoryParameterException {

        Long bookId = bookRepository.getNextId();
        BookBuilder bookBuilder = new BookBuilder()
                .bookId(bookId)
                .readListId(readListId)
                .title(title)
                .lastChapter(lastChapter)
                .note(note)
                .bookStatus(bookStatus)
                .URL(URL);

        if (insertDate != null) {
            bookBuilder.insertDate(insertDate);
            bookBuilder.lastUpdateDate(insertDate);
        } else {
            LocalDateTime tmp = dateFactory.getLocalDateTime();
            bookBuilder.insertDate(tmp);
            bookBuilder.lastUpdateDate(tmp);
        }

        if (bookType != null) {
            bookBuilder.bookType(bookType);
        }

        BookImpl book = bookBuilder.build();

        bookRepository.addOne(book);

        book.setReadingRecordFactory(readingRecordFactory);
        book.setBookRepository(bookRepository);
        book.setDateFactory(dateFactory);

        return book;
    }

    public Book getBook(Long bookId) throws EmptyMandatoryParameterException {
        BookDTO bookDTO = this.bookRepository.getOneDTO(bookId).orElseThrow(EntityNotFoundException::new);

        return fromDTO(bookDTO);
//        List<ReadingRecord> readingRecords = readingRecordFactory.findByBookId(bookId);
//
//        BookBuilder builder = new BookBuilder()
//                .bookId(bookDTO.bookId)
//                .readListId(bookDTO.readListId)
//                .title(bookDTO.title)
//                .bookStatus(bookDTO.bookStatusObj)
//                .insertDate(bookDTO.insertDate)
//                .lastUpdateDate(bookDTO.lastUpdateDate)
//                .lastChapter(bookDTO.lastChapter)
//                .bookType(bookDTO.bookTypeObj.toDomain())
//                .note(bookDTO.note)
//                .readingRecords(readingRecords);
//
//        if (bookDTO.previousBooks != null) {
//            HashMap<Book,Integer> bookOrderMap = bookDTO.previousBooks.stream()
//                    .filter(Objects::nonNull)
//                    .map(item -> new AbstractMap.SimpleImmutableEntry<>(
//                            fromDTO(bookDTO),
//                            item.getOrder()
//                    ))
//                    .collect(
//                            HashMap::new,
//                            (map,item) -> map.put(item.getKey(),item.getValue()),
//                            HashMap::putAll
//                    );
//
//            builder.previousBooks(
//                    new BookChain(bookOrderMap)
//            );
//        }
//
//        BookImpl book = builder.build();
//        book.setReadingRecordFactory(readingRecordFactory);
//        book.setBookRepository(bookRepository);
//        book.setDateFactory(dateFactory);

//        return book;
    }

    public List<Book> getAllChained(Long readListId) {
        List<BookDTO> res = bookRepository.getAllChained(readListId);

        return fromDTO(res);

//        Map<Integer, BookType> id2bookTypeMap = bookTypeFactory.getAll().stream()
//                .collect(
//                    HashMap::new,
//                    (map,item) -> map.put(item.getId(),item),
//                    HashMap::putAll
//                );


//        return res;
    }

    public List<Book> getAll(Long readListId) {
        return fromDTO(bookRepository.getAll(readListId));
    }

//    public Book fromDTO(BookDTO bookDTO) throws EmptyMandatoryParameterException {
//        List<ReadingRecord> readingRecords = readingRecordFactory.findByBookId(bookDTO.bookId);
//
//        BookBuilder builder = new BookBuilder()
//                .bookId(bookDTO.bookId)
//                .readListId(bookDTO.readListId)
//                .title(bookDTO.title)
//                .bookStatus(bookDTO.bookStatusObj)
//                .insertDate(bookDTO.insertDate)
//                .lastUpdateDate(bookDTO.lastUpdateDate)
//                .lastChapter(bookDTO.lastChapter)
//                .bookType(bookDTO.bookTypeObj.toDomain())
//                .note(bookDTO.note)
//                .readingRecords(readingRecords);
//
//        if (bookDTO.previousBooks != null) {
//
//            // TODO
//
//            HashMap<Book, Integer> bookOrderMap = bookDTO.previousBooks.stream()
//                    .filter(Objects::nonNull)
//                    .map(item -> new AbstractMap.SimpleImmutableEntry<>(
//                            fromDTO(bookDTO),
//                            item.getOrder()
//                    ))
//                    .collect(
//                            HashMap::new,
//                            (map, item) -> map.put(item.getKey(), item.getValue()),
//                            HashMap::putAll
//                    );
//
//            builder.previousBooks(
//                    new BookChain(bookOrderMap)
//            );
//        }
//
//        BookImpl book = builder.build();
//        book.setReadingRecordFactory(readingRecordFactory);
//        book.setBookRepository(bookRepository);
//        book.setDateFactory(dateFactory);
//
//        return book;
//    }

    public Book fromDTO(@NonNull BookDTO bookDTO) {
        return fromDTO(List.of(bookDTO)).get(0);
    }

    public List<Book> fromDTO(@NonNull List<BookDTO> bookDTOList) {
        // Preparing reading records
        Map<Long, List<ReadingRecord>> bookId2ReadingRecordsMap = readingRecordFactory.findByBookIds(
                        // Collecting bookIds from chain
                        bookDTOList.stream()
                                // flatten book chain
                                .flatMap(bookDTO -> {
                                    List<BookDTO> tmp = new ArrayList<>();
                                    tmp.add(bookDTO);

                                    if (bookDTO.getPreviousBooks() != null) {
                                        tmp.addAll(
                                                bookDTO.getPreviousBooks().stream()
                                                        .map(BookOrderedDTO::getBookDTO)
                                                        .collect(Collectors.toCollection(ArrayList::new))
                                        );
                                    }

                                    return tmp.stream();
                                })
                                .map(BookDTO::getBookId)
                                .collect(Collectors.toCollection(ArrayList::new))
                ).stream()
                .collect(
                        Collectors.groupingBy(
                                ReadingRecord::getBookId,
                                HashMap::new,
                                Collectors.toCollection(ArrayList::new)
                        )
                );


        return bookDTOList.stream()
                .map(bookDTO -> fromDTO(bookDTO, bookId2ReadingRecordsMap))
                .collect(Collectors.toCollection(ArrayList::new));

    }

    private Book fromDTO(@NonNull BookDTO bookDTO, @NonNull Map<Long, List<ReadingRecord>> bookId2ReadingRecordsMap) throws EmptyMandatoryParameterException {

        log.debug("bookDTO: {}", bookDTO);

        BookBuilder builder = new BookBuilder()
                .bookId(bookDTO.bookId)
                .readListId(bookDTO.readListId)
                .title(bookDTO.title)
                .bookStatus(bookDTO.bookStatusObj)
                .insertDate(bookDTO.insertDate)
                .lastUpdateDate(bookDTO.lastUpdateDate)
                .lastChapter(bookDTO.lastChapter)
                .note(bookDTO.note)
                .URL(bookDTO.URL);

        if (bookDTO.bookTypeObj != null) {
            builder.bookType(bookDTO.bookTypeObj.toDomain());
        }

        if (bookId2ReadingRecordsMap.get(bookDTO.bookId) != null) {
            builder.readingRecords(bookId2ReadingRecordsMap.get(bookDTO.bookId));
        }

        if (bookDTO.previousBooks != null) {

            HashMap<Book, Integer> bookOrderMap = bookDTO.previousBooks.stream()
                    .filter(Objects::nonNull)
                    .map(item -> new AbstractMap.SimpleImmutableEntry<>(
                            fromDTO(item.getBookDTO(), bookId2ReadingRecordsMap),
                            item.getOrder()
                    ))
                    .collect(
                            HashMap::new,
                            (map, item) -> map.put(item.getKey(), item.getValue()),
                            HashMap::putAll
                    );

            builder.previousBooks(
                    new BookChain(bookOrderMap)
            );
        }

        BookImpl book = builder.build();
        book.setReadingRecordFactory(readingRecordFactory);
        book.setBookRepository(bookRepository);
        book.setDateFactory(dateFactory);

        return book;
    }
}

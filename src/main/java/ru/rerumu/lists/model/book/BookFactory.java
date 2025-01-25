package ru.rerumu.lists.model.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.BookChain;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.book.type.BookType;
import ru.rerumu.lists.model.books.reading_records.ReadingRecordFactory;
import ru.rerumu.lists.repository.BookRepository;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Objects;

@Component
public class BookFactory {

    private final DateFactory dateFactory;
    private final BookRepository bookRepository;
    private final ReadingRecordFactory readingRecordFactory;

    @Autowired
    public BookFactory(DateFactory dateFactory, BookRepository bookRepository, ReadingRecordFactory readingRecordFactory) {
        this.dateFactory = dateFactory;
        this.bookRepository = bookRepository;
        this.readingRecordFactory = readingRecordFactory;
    }

    public Book createBook(
            Long readListId,
            String title,
            Integer lastChapter,
            String note,
            BookStatusRecord bookStatus,
            LocalDateTime insertDate,
            BookType bookType
    ) throws EmptyMandatoryParameterException {

        Long bookId = bookRepository.getNextId();
        BookBuilder bookBuilder = new BookBuilder()
                .bookId(bookId)
                .readListId(readListId)
                .title(title)
                .lastChapter(lastChapter)
                .note(note)
                .bookStatus(bookStatus);

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

        BookBuilder builder = new BookBuilder()
                .bookId(bookDTO.bookId)
                .readListId(bookDTO.readListId)
                .title(bookDTO.title)
                .bookStatus(bookDTO.bookStatusObj)
                .insertDate(bookDTO.insertDate)
                .lastUpdateDate(bookDTO.lastUpdateDate)
                .lastChapter(bookDTO.lastChapter)
                .bookType(bookDTO.bookTypeObj.toDomain())
                .note(bookDTO.note)
                .readingRecords(bookDTO.readingRecords);

        if (bookDTO.previousBooks != null) {
            HashMap<Book,Integer> bookOrderMap = bookDTO.previousBooks.stream()
                    .filter(Objects::nonNull)
                    .map(item -> new AbstractMap.SimpleImmutableEntry<>(
                            item.bookDTO.toDomain(),
                            item.getOrder()
                    ))
                    .collect(
                            HashMap::new,
                            (map,item) -> map.put(item.getKey(),item.getValue()),
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

    public Book fromDTO(BookDTO bookDTO) throws EmptyMandatoryParameterException {

        BookBuilder builder = new BookBuilder()
                .bookId(bookDTO.bookId)
                .readListId(bookDTO.readListId)
                .title(bookDTO.title)
                .bookStatus(bookDTO.bookStatusObj)
                .insertDate(bookDTO.insertDate)
                .lastUpdateDate(bookDTO.lastUpdateDate)
                .lastChapter(bookDTO.lastChapter)
                .bookType(bookDTO.bookTypeObj.toDomain())
                .note(bookDTO.note)
                .readingRecords(bookDTO.readingRecords);

        if (bookDTO.previousBooks != null) {
            HashMap<Book,Integer> bookOrderMap = bookDTO.previousBooks.stream()
                    .filter(Objects::nonNull)
                    .map(item -> new AbstractMap.SimpleImmutableEntry<>(
                            fromDTO(bookDTO),
                            item.getOrder()
                    ))
                    .collect(
                            HashMap::new,
                            (map,item) -> map.put(item.getKey(),item.getValue()),
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

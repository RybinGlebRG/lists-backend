package ru.rerumu.lists.model.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.model.books.reading_records.ReadingRecordFactory;
import ru.rerumu.lists.repository.BookRepository;

import java.time.LocalDateTime;
import java.util.Optional;

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

        return book;
    }

    public Book getBook(Long bookId){
        BookImpl book = this.bookRepository.getOne(bookId).orElseThrow(EntityNotFoundException::new);
        book.setReadingRecordFactory(readingRecordFactory);
        return book;
    }
}

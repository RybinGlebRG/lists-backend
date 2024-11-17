package ru.rerumu.lists.model.book;

import lombok.NonNull;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.BookChain;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

public class BookBuilder {
    private Long bookId;
    private Long readListId;
    private String title;
    private BookStatusRecord bookStatus;
    private Date insertDate;
    private Date lastUpdateDate;
    private Integer lastChapter;

    private BookType bookType;

    private BookChain previousBooks;

    private String note;
    private List<ReadingRecord> readingRecords;

    public BookBuilder() {
    }

    public BookBuilder(BookImpl book) {
        this.bookId = book.getBookId();
        this.readListId = book.getReadListId();
        this.title = book.getTitle();
        this.bookStatus = book.getBookStatus();
        this.insertDate = book.getInsertDate();
        this.lastUpdateDate = book.getLastUpdateDate();
        this.lastChapter = book.getLastChapter().orElse(null);
        this.previousBooks = book.getPreviousBooks();
        this.note = book.getNote();
    }

    public BookBuilder bookId(Long bookId) {
        this.bookId = bookId;
        return this;
    }

    public BookBuilder readListId(Long readListId) {
        this.readListId = readListId;
        return this;
    }

    public BookBuilder title(String title) {
        this.title = title;
        return this;
    }

    public BookBuilder bookStatus(BookStatusRecord bookStatus) {
        this.bookStatus = bookStatus;
        return this;
    }

    public BookBuilder insertDate(Date insertDate) {
        this.insertDate = insertDate;
        return this;
    }

    public BookBuilder insertDate(LocalDateTime insertDate) {
        this.insertDate = Date.from(insertDate.toInstant(ZoneOffset.UTC));
        return this;
    }

    public BookBuilder lastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
        return this;
    }

    public BookBuilder lastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = Date.from(lastUpdateDate.toInstant(ZoneOffset.UTC));
        return this;
    }

    public BookBuilder lastChapter(Integer lastChapter) {
        this.lastChapter = lastChapter;
        return this;
    }

    public BookBuilder bookType(BookType bookType) {
        this.bookType = bookType;
        return this;
    }

    public BookBuilder previousBooks(BookChain previousBooks) {
        this.previousBooks = previousBooks;
        return this;
    }

    public BookBuilder note(String note){
        this.note = note;
        return this;
    }

    public BookBuilder readingRecords(@NonNull List<ReadingRecord> readingRecords){
        this.readingRecords = readingRecords;
        return this;
    }


    public BookImpl build() throws EmptyMandatoryParameterException {
        return new BookImpl(
                bookId,
                readListId,
                title,
                bookStatus,
                insertDate,
                lastUpdateDate,
                lastChapter,
                bookType,
                previousBooks,
                note,
                readingRecords
        );
    }
}

package ru.rerumu.lists.model.book.impl;

import lombok.NonNull;
import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.crosscut.utils.DateFactory;
import ru.rerumu.lists.model.BookChain;
import ru.rerumu.lists.model.book.readingrecords.status.BookStatusRecord;
import ru.rerumu.lists.model.book.readingrecords.status.StatusFactory;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.book.readingrecords.ReadingRecord;
import ru.rerumu.lists.model.book.type.BookType;
import ru.rerumu.lists.model.tag.Tag;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
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
    private List<ReadingRecord> readingRecords = new ArrayList<>();
    private String URL;
    private User user;
    private List<Tag> tags = new ArrayList<>();

    private final StatusFactory statusFactory;
    private final DateFactory dateFactory;

    public BookBuilder(
            @NonNull StatusFactory statusFactory,
            @NonNull DateFactory dateFactory
    ) {
        this.statusFactory = statusFactory;
        this.dateFactory = dateFactory;
    }

    public BookBuilder bookId(@NonNull Long bookId) {
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

    public BookBuilder URL(String URL){
        this.URL = URL;
        return this;
    }

    public BookBuilder user(User user){
        this.user = user;
        return this;
    }

    public BookBuilder tags(@NonNull List<Tag> tags){
        this.tags = tags;
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
                readingRecords,
                statusFactory,
                URL,
                user,
                tags,
                dateFactory
        );
    }
}

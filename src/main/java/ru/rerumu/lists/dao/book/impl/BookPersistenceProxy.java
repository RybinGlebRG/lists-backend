package ru.rerumu.lists.dao.book.impl;

import lombok.NonNull;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.dao.base.PersistenceProxy;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.book.BookChain;
import ru.rerumu.lists.domain.booktype.BookType;
import ru.rerumu.lists.domain.readingrecord.ReadingRecord;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class BookPersistenceProxy extends PersistenceProxy<Book> implements Book {

    private final Book book;

    public BookPersistenceProxy(Book book, EntityState entityState) {
        super(entityState);
        this.book = book;

        // If persisted, then should have persisted copy
        if (entityState.equals(EntityState.PERSISTED)) {
            initPersistedCopy();
        }
    }

    @Override
    public void addReadingRecord(ReadingRecord readingRecord) {
        book.addReadingRecord(readingRecord);
    }

    @Override
    public ReadingRecord deleteReadingRecord(Long readingRecordId) {
        return book.deleteReadingRecord(readingRecordId);
    }

    @Override
    public void updateReadingRecords(@NonNull List<ReadingRecord> readingRecords) {
        if (!readingRecords.equals(book.getReadingRecords())) {
            book.updateReadingRecords(readingRecords);
            entityState = EntityState.DIRTY;
        }
    }

    @Override
    public Long getId() {
        return book.getId();
    }

    @Override
    public User getUser() {
        return book.getUser();
    }

    @Override
    public void save() {
        throw new NotImplementedException();
    }

    @Override
    public List<ReadingRecord> getReadingRecords() {
        return book.getReadingRecords();
    }

    @Override
    public List<Series> getSeriesList() {
        return book.getSeriesList();
    }

    @Override
    public BookChain getPreviousBooks() {
        return book.getPreviousBooks();
    }

    @Override
    public BookType getBookType() {
        return book.getBookType();
    }

    @Override
    public List<Tag> getTags() {
        return book.getTags();
    }

    @Override
    public List<Author> getTextAuthors() {
        return book.getTextAuthors();
    }

    @Override
    public String getURL() {
        return book.getURL();
    }

    @Override
    public String getTitle() {
        return book.getTitle();
    }

    @Override
    public LocalDateTime getInsertDate() {
        return book.getInsertDate();
    }

    @Override
    public LocalDateTime getAddedDate() {
        return book.getAddedDate();
    }

    @Override
    public void updateInsertDate(LocalDateTime insertDate) {
        book.updateInsertDate(insertDate);
    }

    @Override
    public void updateTitle(String title) {
        book.updateTitle(title);
    }

    @Override
    public void updateNote(String note) {
        book.updateNote(note);
    }

    @Override
    public void updateType(BookType bookType) {
        book.updateType(bookType);
    }

    @Override
    public void updateURL(String URL) {
        book.updateURL(URL);
    }

    @Override
    public void updateTags(List<Tag> tags) {
        book.updateTags(tags);
    }

    @Override
    public void updateSeries(@NonNull List<Series> seriesList) {
        book.updateSeries(seriesList);
    }

    @Override
    public void updateTextAuthors(List<Author> authors) {
        book.updateTextAuthors(authors);
    }

    @Override
    public Float getTitleFuzzyMatchScore(String value) {
        return book.getTitleFuzzyMatchScore(value);
    }

    @Override
    public String getNote() {
        return book.getNote();
    }

    @Override
    public boolean currentStatusEquals(Long statusId) {
        return book.currentStatusEquals(statusId);
    }

    @Override
    public void delete() {
        book.delete();
    }

    @Override
    public EntityState getEntityState() {
        return entityState;
    }

    @Override
    public Book getPersistedCopy() {
        return persistedCopy;
    }

    @Override
    public void initPersistedCopy() {
        persistedCopy = book.deepCopy();
    }

    @Override
    public void clearPersistedCopy() {
        persistedCopy = null;
    }

    @Override
    public Book deepCopy() {
        return new BookPersistenceProxy(book.deepCopy(), entityState);
    }
}

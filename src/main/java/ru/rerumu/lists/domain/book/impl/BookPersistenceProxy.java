package ru.rerumu.lists.domain.book.impl;

import lombok.NonNull;
import org.json.JSONObject;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.base.PersistenceProxy;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.book.BookDTO;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;
import ru.rerumu.lists.domain.booktype.BookType;
import ru.rerumu.lists.domain.readingrecords.ReadingRecord;
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
    public Long getListId() {
        return book.getListId();
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
        book.save();

        // Reinit persisted copy after saving
        entityState = EntityState.PERSISTED;
        initPersistedCopy();
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
    public void updateLastChapter(Integer lastChapter) {
        book.updateLastChapter(lastChapter);
    }

    @Override
    public void updateStatus(BookStatusRecord bookStatusRecord) {
        book.updateStatus(bookStatusRecord);
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
    public boolean filterByStatusIds(List<Integer> statusIds) {
        return book.filterByStatusIds(statusIds);
    }

    @Override
    public Float getTitleFuzzyMatchScore(String value) {
        return book.getTitleFuzzyMatchScore(value);
    }

    @Override
    public BookDTO toDTO() {
        return book.toDTO();
    }

    @Override
    public JSONObject toJSONObject() {
        return book.toJSONObject();
    }

    @Override
    public LocalDateTime getUpdateDate() {
        return book.getUpdateDate();
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
        return book.deepCopy();
    }
}

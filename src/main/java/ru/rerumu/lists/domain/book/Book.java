package ru.rerumu.lists.domain.book;

import lombok.NonNull;
import ru.rerumu.lists.crosscut.DeepCopyable;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.base.Entity;
import ru.rerumu.lists.domain.booktype.BookType;
import ru.rerumu.lists.domain.readingrecord.ReadingRecord;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.seriesitem.SeriesItem;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface Book extends SeriesItem, Entity, DeepCopyable<Book> {

    /**
     * Add reading record to book
     */
    void addReadingRecord(ReadingRecord readingRecord);

    ReadingRecord deleteReadingRecord(Long readingRecordId);

    void updateReadingRecords(List<ReadingRecord> readingRecords);

    User getUser();
    List<ReadingRecord> getReadingRecords();
    List<Series> getSeriesList();
    BookChain getPreviousBooks();
    BookType getBookType();
    List<Tag> getTags();
    List<Author> getTextAuthors();
    String getURL();
    String getTitle();
    LocalDateTime getInsertDate();
    String getNote();

    /**
     * Date when book was added
     */
    LocalDateTime getAddedDate();

    void updateInsertDate(LocalDateTime insertDate);
    void updateTitle(String title);

    void updateNote(String note);
    void updateType(BookType bookType);
    void updateURL(String URL);
    void updateTags(List<Tag> tags);
    void updateSeries(@NonNull List<Series> seriesList);

    /**
     * Updates text authors.
     */
    void updateTextAuthors(List<Author> authors);

    Float getTitleFuzzyMatchScore(String value);

    boolean currentStatusEquals(Long statusId);

    boolean removeReadingRecord(ReadingRecord readingRecord);

}

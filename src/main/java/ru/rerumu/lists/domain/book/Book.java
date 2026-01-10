package ru.rerumu.lists.domain.book;

import lombok.NonNull;
import ru.rerumu.lists.crosscut.DeepCopyable;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.base.Entity;
import ru.rerumu.lists.domain.booktype.BookType;
import ru.rerumu.lists.domain.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.readingrecordstatus.ReadingRecordStatuses;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.item.SeriesItem;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface Book extends SeriesItem, Entity<Book>, DeepCopyable<Book> {

    /**
     * Add reading record to book
     */
    void addReadingRecord(ReadingRecord readingRecord);

    ReadingRecord deleteReadingRecord(Long readingRecordId);

    void updateReadingRecords(List<ReadingRecord> readingRecords);

    Long getListId();
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
    LocalDateTime getUpdateDate();
    Integer getLastChapter();
    String getNote();
    ReadingRecordStatuses getBookStatus();

    /**
     * Date when book was added
     */
    LocalDateTime getAddedDate();

    void updateInsertDate(LocalDateTime insertDate);
    void updateTitle(String title);

    @Deprecated
    void updateLastChapter(Integer lastChapter);

    @Deprecated
    void updateStatus(ReadingRecordStatuses bookStatusRecord);
    void updateNote(String note);
    void updateType(BookType bookType);
    void updateURL(String URL);
    void updateTags(List<Tag> tags);
    void updateSeries(@NonNull List<Series> seriesList);

    /**
     * Updates text authors.
     */
    void updateTextAuthors(List<Author> authors);

    boolean filterByStatusIds(List<Integer> statusIds);
    Float getTitleFuzzyMatchScore(String value);

    boolean currentStatusEquals(Long statusId);

}

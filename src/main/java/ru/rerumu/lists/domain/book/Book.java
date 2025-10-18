package ru.rerumu.lists.domain.book;

import lombok.NonNull;
import org.json.JSONObject;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.base.Entity;
import ru.rerumu.lists.domain.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.readingrecords.RecordDTO;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;
import ru.rerumu.lists.domain.booktype.BookType;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.item.SeriesItem;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface Book extends SeriesItem, Entity {

    void addReadingRecord(
            @NonNull BookStatusRecord bookStatusRecord,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    );

    void addReadingRecord(
            @NonNull Long statusId,
            @NonNull LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    );

    ReadingRecord deleteReadingRecord(Long readingRecordId);

    void updateReadingRecords(List<RecordDTO> records);

    Long getListId();
    User getUser();

    void updateInsertDate(LocalDateTime insertDate);
    void updateTitle(String title);

    @Deprecated
    void updateLastChapter(Integer lastChapter);

    @Deprecated
    void updateStatus(BookStatusRecord bookStatusRecord);
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

    BookDTO toDTO();
    JSONObject toJSONObject();

    boolean currentStatusEquals(Long statusId);


    void delete();
}

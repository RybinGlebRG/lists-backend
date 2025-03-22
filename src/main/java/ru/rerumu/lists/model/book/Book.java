package ru.rerumu.lists.model.book;

import lombok.NonNull;
import org.json.JSONObject;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.book.readingrecords.ReadingRecord;
import ru.rerumu.lists.model.book.type.BookType;
import ru.rerumu.lists.model.series.item.SeriesItem;
import ru.rerumu.lists.model.tag.Tag;

import java.time.LocalDateTime;
import java.util.List;

public interface Book extends SeriesItem {

    void addReadingRecord(
            @NonNull BookStatusRecord bookStatusRecord,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    );

    ReadingRecord deleteReadingRecord(Long readingRecordId);

    void updateReadingRecord(
            @NonNull Long readingRecordId,
            @NonNull BookStatusRecord bookStatusRecord,
            @NonNull LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    );

    Long getId();
    Long getListId();
    User getUser();

    void updateInsertDate(LocalDateTime insertDate);
    void updateTitle(String title);

    @Deprecated
    void updateLastChapter(Integer lastChapter);

    void updateStatus(BookStatusRecord bookStatusRecord);
    void updateNote(String note);
    void updateType(BookType bookType);
    void updateURL(String URL);
    void updateTags(List<Tag> tags);
    void save();

    boolean filterByStatusIds(List<Integer> statusIds);
    Float getTitleFuzzyMatchScore(String value);

    String toString();
    BookDTO toDTO();
    JSONObject toJSONObject();
}

package ru.rerumu.lists.model.book;

import lombok.NonNull;
import org.json.JSONObject;
import ru.rerumu.lists.model.book.readingrecords.RecordDTO;
import ru.rerumu.lists.model.book.readingrecords.status.BookStatusRecord;
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

    void addReadingRecord(
            @NonNull Long statusId,
            @NonNull LocalDateTime startDate,
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
    void updateReadingRecord(
            @NonNull Long readingRecordId,
            @NonNull Long statusId,
            @NonNull LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    );

    void updateReadingRecords(List<RecordDTO> records);

    /**
     * Deletes reading records that are not passed in the {@code readingRecordIdsToKeep} parameter.
     *
     * @param readingRecordIdsToKeep Reading records to keep
     */
    void deleteOtherReadingRecords(List<Long> readingRecordIdsToKeep);

    Long getId();
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
    void save();

    boolean filterByStatusIds(List<Integer> statusIds);
    Float getTitleFuzzyMatchScore(String value);

    String toString();
    BookDTO toDTO();
    JSONObject toJSONObject();
}

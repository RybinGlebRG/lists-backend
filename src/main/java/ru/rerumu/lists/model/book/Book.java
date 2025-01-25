package ru.rerumu.lists.model.book;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import org.json.JSONObject;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.book.type.BookType;
import ru.rerumu.lists.model.series.item.SeriesItem;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface Book extends SeriesItem {

    ReadingRecord addReadingRecord(
            Long bookId,
            Long readingRecordId,
            BookStatusRecord bookStatusRecord,
            LocalDateTime startDate,
            @Nullable LocalDateTime endDate
    );

    void addReadingRecord(
            @NonNull BookStatusRecord bookStatusRecord,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    ReadingRecord deleteReadingRecord(Long readingRecordId);

    ReadingRecord updateReadingRecord(
            Long readingRecordId,
            BookStatusRecord bookStatusRecord,
            LocalDateTime startDate,
            @Nullable LocalDateTime endDate
    );

    Long getId();
    Long getListId();

    void updateInsertDate(LocalDateTime insertDate);
    void updateTitle(String title);
    void updateLastChapter(Integer lastChapter);
    void updateStatus(BookStatusRecord bookStatusRecord);
    void updateNote(String note);
    void updateType(BookType bookType);
    void save();

    boolean filterByStatusIds(List<Integer> statusIds);
    Float getTitleFuzzyMatchScore(String value);

    String toString();
    BookDTO toDTO();
    JSONObject toJSONObject();
}

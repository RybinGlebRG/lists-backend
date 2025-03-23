package ru.rerumu.lists.model.book.readingrecords;

import lombok.NonNull;
import org.json.JSONObject;
import ru.rerumu.lists.model.book.readingrecords.status.BookStatusRecord;

import java.time.LocalDateTime;

public interface ReadingRecord extends Comparable<ReadingRecord>{

    Long getId();
    Long getBookId();

    void save();
    void delete();

    void setStatus(@NonNull BookStatusRecord bookStatusRecord);
    void setStartDate(@NonNull LocalDateTime startDate);
    void setEndDate(LocalDateTime endDate);
    void setLastChapter(Long lastChapter);

    ReadingRecordDTO toDTO();
    JSONObject toJSONObject();
}

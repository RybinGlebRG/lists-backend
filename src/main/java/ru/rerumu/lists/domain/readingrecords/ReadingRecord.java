package ru.rerumu.lists.domain.readingrecords;

import lombok.NonNull;
import org.json.JSONObject;
import ru.rerumu.lists.crosscut.DeepCopyable;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;

import java.time.LocalDateTime;

public interface ReadingRecord extends DeepCopyable<ReadingRecord> {

    Long getId();
    Long getBookId();
    LocalDateTime getUpdateDate();
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
    BookStatusRecord getBookStatus();
    Long getLastChapter();
    Boolean getIsMigrated();

    void update(
            @NonNull BookStatusRecord bookStatusRecord,
            @NonNull LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    );

    boolean statusEquals(@NonNull Long statusId);

    JSONObject toJSONObject();
}

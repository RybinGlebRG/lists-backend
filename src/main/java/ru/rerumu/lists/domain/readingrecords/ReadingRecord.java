package ru.rerumu.lists.domain.readingrecords;

import lombok.NonNull;
import org.json.JSONObject;
import ru.rerumu.lists.crosscut.DeepCopyable;
import ru.rerumu.lists.domain.readingrecordstatus.ReadingRecordStatuses;

import java.time.LocalDateTime;

public interface ReadingRecord extends DeepCopyable<ReadingRecord> {

    Long getId();
    Long getBookId();
    LocalDateTime getUpdateDate();
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
    ReadingRecordStatuses getBookStatus();
    Long getLastChapter();
    Boolean getIsMigrated();

    /**
     * Update reading record
     * @return {@code true} if changes were made, {@code false} otherwise
     */
    boolean update(
            @NonNull ReadingRecordStatuses bookStatusRecord,
            @NonNull LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    );

    boolean statusEquals(@NonNull Long statusId);

    JSONObject toJSONObject();
}

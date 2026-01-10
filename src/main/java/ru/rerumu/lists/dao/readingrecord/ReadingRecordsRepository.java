package ru.rerumu.lists.dao.readingrecord;

import lombok.NonNull;
import ru.rerumu.lists.domain.readingrecord.ReadingRecord;
import ru.rerumu.lists.domain.readingrecordstatus.ReadingRecordStatuses;

import java.time.LocalDateTime;
import java.util.List;

public interface ReadingRecordsRepository {
    List<ReadingRecord> findByBookId(Long bookId);
    List<ReadingRecord> findByBookIds(List<Long> bookIds);

    @Deprecated
    void delete(Long readingRecordId);

    /**
     * Delete reading record
     */
    void delete(ReadingRecord readingRecord);

    /**
     * Get next id for entity
     */
    Long getNextId();

    ReadingRecord create(
            @NonNull Long bookId,
            @NonNull ReadingRecordStatuses bookStatusRecord,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    );

    ReadingRecord create(
            @NonNull Long bookId,
            @NonNull Long statusId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    );

    ReadingRecord findById(@NonNull Long id);

    void update(ReadingRecord readingRecord);

    /**
     * Wraps instance of ReadingRecord in persistence proxy
     */
    ReadingRecord attach(ReadingRecord readingRecord);
}

package ru.rerumu.lists.model;

import jakarta.annotation.Nullable;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;

import java.time.LocalDateTime;

public interface Book {

    ReadingRecord addReadingRecord(
            Long bookId,
            Long readingRecordId,
            BookStatusRecord bookStatusRecord,
            LocalDateTime startDate,
            @Nullable LocalDateTime endDate
    );
}

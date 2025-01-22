package ru.rerumu.lists.model.book;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import ru.rerumu.lists.model.BookStatusRecord;
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

    void addReadingRecord(
            @NonNull BookStatusRecord bookStatusRecord,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    Long getId();
}

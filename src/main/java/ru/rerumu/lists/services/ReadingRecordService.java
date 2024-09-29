package ru.rerumu.lists.services;

import ru.rerumu.lists.model.books.reading_records.ReadingRecord;
import ru.rerumu.lists.views.ReadingRecordAddView;
import ru.rerumu.lists.views.ReadingRecordUpdateView;

import java.util.List;
import java.util.Optional;

public interface ReadingRecordService {

    ReadingRecord addRecord(Long bookId, ReadingRecordAddView readingRecordAddView);
    ReadingRecord addRecord(ReadingRecord readingRecord);
    Long getNextId();

    ReadingRecord addRecord(Long bookId, ReadingRecord readingRecord);
    ReadingRecord updateRecord(Long recordId, ReadingRecordUpdateView readingRecordUpdateView);
    ReadingRecord updateRecord(ReadingRecord readingRecord);

    void deleteRecord(Long recordId);
    List<ReadingRecord> getReadingRecords(Long bookId);
    Optional<ReadingRecord> getReadingRecord(Long readingRecordId);
}

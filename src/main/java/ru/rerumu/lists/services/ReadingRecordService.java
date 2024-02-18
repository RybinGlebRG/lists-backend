package ru.rerumu.lists.services;

import ru.rerumu.lists.model.books.reading_records.ReadingRecord;
import ru.rerumu.lists.views.ReadingRecordAddView;
import ru.rerumu.lists.views.ReadingRecordUpdateView;

import java.util.List;

public interface ReadingRecordService {

    void addRecord(Long bookId, ReadingRecordAddView readingRecordAddView);
    void updateRecord(Long recordId, ReadingRecordUpdateView readingRecordUpdateView);

    void deleteRecord(Long recordId);
    List<ReadingRecord> getReadingRecords(Long bookId);
}

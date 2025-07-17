package ru.rerumu.lists.services.book.readingrecord;

import ru.rerumu.lists.domain.book.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.book.readingrecords.impl.ReadingRecordImpl;
import ru.rerumu.lists.views.ReadingRecordAddView;
import ru.rerumu.lists.views.ReadingRecordUpdateView;

import java.util.List;
import java.util.Optional;

public interface ReadingRecordService {

    ReadingRecordImpl addRecord(Long bookId, ReadingRecordAddView readingRecordAddView);
//    ReadingRecordImpl addRecord(ReadingRecordImpl readingRecord);
    Long getNextId();

//    ReadingRecordImpl addRecord(Long bookId, ReadingRecordImpl readingRecord);
    ReadingRecord updateRecord(Long recordId, ReadingRecordUpdateView readingRecordUpdateView);
    ReadingRecordImpl updateRecord(ReadingRecordImpl readingRecord);

    void deleteRecord(Long recordId);
    List<ReadingRecord> getReadingRecords(Long bookId);
    Optional<ReadingRecord> getReadingRecord(Long readingRecordId);
}

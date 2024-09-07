package ru.rerumu.lists.services;

import com.jcabi.aspects.Loggable;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;
import ru.rerumu.lists.repository.ReadingRecordsRepository;
import ru.rerumu.lists.views.ReadingRecordAddView;
import ru.rerumu.lists.views.ReadingRecordUpdateView;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class ReadingRecordServiceImpl implements ReadingRecordService {

    private final ReadingRecordsRepository crudRepository;

    private final BookStatusesService bookStatusesService;

    public ReadingRecordServiceImpl(ReadingRecordsRepository crudRepository, BookStatusesService bookStatusesService) {
        this.crudRepository = crudRepository;
        this.bookStatusesService = bookStatusesService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReadingRecord addRecord(Long bookId, ReadingRecordAddView readingRecordAddView) {
        Objects.requireNonNull(bookId, "bookId cannot be null");
        Objects.requireNonNull(readingRecordAddView, "readingRecordAddView cannot be null");

        BookStatusRecord bookStatusRecord = bookStatusesService.findById(readingRecordAddView.statusId()).orElseThrow();

        ReadingRecord readingRecord = ReadingRecord.builder()
                .bookStatus(bookStatusRecord)
                .startDate(readingRecordAddView.startDate())
                .endDate(readingRecordAddView.endDate())
                .build();

        addRecord(bookId, readingRecord);
        return readingRecord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReadingRecord addRecord(Long bookId, ReadingRecord readingRecord) {
        if (readingRecord.recordId() == null){
            readingRecord = readingRecord.toBuilder()
                    .recordId(crudRepository.getNextId())
                    .build();
        }
        crudRepository.create(readingRecord);
        return readingRecord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Loggable(value = Loggable.DEBUG, prepend = true, trim = false)
    public ReadingRecord updateRecord(Long recordId, ReadingRecordUpdateView readingRecordUpdateView) {
        Objects.requireNonNull(recordId, "recordId cannot be null");
        Objects.requireNonNull(readingRecordUpdateView, "readingRecordUpdateView cannot be null");

        ReadingRecord readingRecord = crudRepository.findById(recordId).orElseThrow();

        BookStatusRecord bookStatusRecord = bookStatusesService.findById(readingRecordUpdateView.statusId()).orElseThrow();

        readingRecord = readingRecord.toBuilder()
                .bookStatus(bookStatusRecord)
                .startDate(readingRecordUpdateView.startDate())
                .endDate(readingRecordUpdateView.endDate())
                .build();

        crudRepository.update(readingRecord);

        return readingRecord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRecord(Long recordId) {
        crudRepository.delete(recordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<ReadingRecord> getReadingRecords(Long bookId) {
        return crudRepository.findByBookId(bookId);
    }

    @Override
    public Optional<ReadingRecord> getReadingRecord(Long readingRecordId) {
        return crudRepository.findById(readingRecordId);
    }
}

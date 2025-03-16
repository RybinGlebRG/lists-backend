package ru.rerumu.lists.services.book.reading_record.impl;

import com.jcabi.aspects.Loggable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.book.reading_records.ReadingRecord;
import ru.rerumu.lists.model.book.reading_records.impl.ReadingRecordFactory;
import ru.rerumu.lists.model.book.reading_records.impl.ReadingRecordImpl;
import ru.rerumu.lists.dao.reading_record.ReadingRecordsRepository;
import ru.rerumu.lists.services.book.status.BookStatusesService;
import ru.rerumu.lists.services.book.reading_record.ReadingRecordService;
import ru.rerumu.lists.views.ReadingRecordAddView;
import ru.rerumu.lists.views.ReadingRecordUpdateView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class ReadingRecordServiceImpl implements ReadingRecordService {

    private final ReadingRecordsRepository crudRepository;

    private final BookStatusesService bookStatusesService;

    private final ReadingRecordFactory readingRecordFactory;

    public ReadingRecordServiceImpl(ReadingRecordsRepository crudRepository, BookStatusesService bookStatusesService, ReadingRecordFactory readingRecordFactory) {
        this.crudRepository = crudRepository;
        this.bookStatusesService = bookStatusesService;
        this.readingRecordFactory = readingRecordFactory;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReadingRecordImpl addRecord(Long bookId, ReadingRecordAddView readingRecordAddView) {
        Objects.requireNonNull(bookId, "bookId cannot be null");
        Objects.requireNonNull(readingRecordAddView, "readingRecordAddView cannot be null");

        BookStatusRecord bookStatusRecord = bookStatusesService.findById(readingRecordAddView.statusId()).orElseThrow();

        ReadingRecordImpl readingRecord = readingRecordFactory.createReadingRecord(
                bookId,
                bookStatusRecord,
                readingRecordAddView.startDate(),
                readingRecordAddView.endDate(),
                readingRecordAddView.lastChapter()
        );

        return readingRecord;
    }

//    @Override
//    public ReadingRecordImpl addRecord(ReadingRecordImpl readingRecord) {
//        crudRepository.create(readingRecord);
//
//        return readingRecord;
//    }

    @Override
    public Long getNextId() {
        return crudRepository.getNextId();
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public ReadingRecordImpl addRecord(Long bookId, ReadingRecordImpl readingRecord) {
//        if (readingRecord.getRecordId() == null){
//            readingRecord = readingRecord.toBuilder()
//                    .recordId(crudRepository.getNextId())
//                    .build();
//        }
//        crudRepository.create(readingRecord);
//        return readingRecord;
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Loggable(value = Loggable.DEBUG, prepend = true, trim = false)
    public ReadingRecord updateRecord(Long recordId, ReadingRecordUpdateView readingRecordUpdateView) {
        Objects.requireNonNull(recordId, "recordId cannot be null");
        Objects.requireNonNull(readingRecordUpdateView, "readingRecordUpdateView cannot be null");

        ReadingRecord readingRecord = crudRepository.findById(recordId).map(readingRecordFactory::fromDTO).orElseThrow();

        BookStatusRecord bookStatusRecord = bookStatusesService.findById(readingRecordUpdateView.statusId()).orElseThrow();

        readingRecord.setStatus(bookStatusRecord);
        readingRecord.setStartDate(readingRecordUpdateView.startDate());
        readingRecord.setEndDate(readingRecordUpdateView.endDate());

        readingRecord.save();

        return readingRecord;
    }

    @Override
    public ReadingRecordImpl updateRecord(ReadingRecordImpl readingRecord) {
        crudRepository.update(readingRecord.toDTO());
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
        return crudRepository.findByBookId(bookId).stream()
                .peek(readingRecordDTO -> log.debug("readingRecordDTO: {}", readingRecordDTO))
                .map(readingRecordFactory::fromDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Optional<ReadingRecord> getReadingRecord(Long readingRecordId) {
        return crudRepository.findById(readingRecordId).map(readingRecordFactory::fromDTO);
    }
}

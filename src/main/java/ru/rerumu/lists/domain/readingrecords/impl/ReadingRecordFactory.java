package ru.rerumu.lists.domain.readingrecords.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.book.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;
import ru.rerumu.lists.domain.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.readingrecords.ReadingRecordDTO;
import ru.rerumu.lists.crosscut.utils.DateFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReadingRecordFactory {

    private final DateFactory dateFactory;
    private final ReadingRecordsRepository readingRecordsRepository;

    @Autowired
    public ReadingRecordFactory(
            DateFactory dateFactory,
            ReadingRecordsRepository readingRecordsRepository
    ) {
        this.dateFactory = dateFactory;
        this.readingRecordsRepository = readingRecordsRepository;
    }

    public ReadingRecord createReadingRecord(
            @NonNull Long bookId,
            @NonNull BookStatusRecord bookStatusRecord,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    ){

        Long readingRecordId = readingRecordsRepository.getNextId();

        if (startDate == null) {
            startDate = dateFactory.getLocalDateTime();
        }

        ReadingRecordImpl readingRecord = new ReadingRecordImpl(
                readingRecordId,
                bookId,
                bookStatusRecord,
                startDate,
                endDate,
                false,
                lastChapter,
                readingRecordsRepository,
                dateFactory
        );

        readingRecordsRepository.create(readingRecord.toDTO());

        ReadingRecordPersistenceProxy readingRecordPersistenceProxy = new ReadingRecordPersistenceProxy(readingRecord, EntityState.NEW);
        readingRecordPersistenceProxy.initPersistedCopy();

        return readingRecordPersistenceProxy;
    }

    public ReadingRecord fromDTO(@NonNull ReadingRecordDTO readingRecordDTO){
        ReadingRecordImpl readingRecord = new ReadingRecordImpl(
                readingRecordDTO.recordId(),
                readingRecordDTO.bookId(),
                readingRecordDTO.bookStatus(),
                readingRecordDTO.startDate(),
                readingRecordDTO.endDate(),
                readingRecordDTO.isMigrated(),
                readingRecordDTO.lastChapter(),
                readingRecordsRepository,
                dateFactory
        );

        ReadingRecordPersistenceProxy readingRecordPersistenceProxy = new ReadingRecordPersistenceProxy(
                readingRecord,
                EntityState.PERSISTED
        );
        readingRecordPersistenceProxy.initPersistedCopy();

        return readingRecordPersistenceProxy;
    }

    public List<ReadingRecord> findByBookId(@NonNull Long bookId){
        return readingRecordsRepository.findByBookId(bookId).stream()
                .map(this::fromDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // TODO: pagination
    public List<ReadingRecord> findByBookIds(@NonNull List<Long> bookIds){
        return readingRecordsRepository.findByBookIds(bookIds).stream()
                .map(this::fromDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}

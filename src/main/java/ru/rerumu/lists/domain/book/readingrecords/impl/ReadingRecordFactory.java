package ru.rerumu.lists.domain.book.readingrecords.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.book.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;
import ru.rerumu.lists.domain.book.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.book.readingrecords.ReadingRecordDTO;
import ru.rerumu.lists.services.book.readingrecord.ReadingRecordService;
import ru.rerumu.lists.crosscut.utils.DateFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReadingRecordFactory {

    private final ReadingRecordService readingRecordService;
    private final DateFactory dateFactory;
    private final ReadingRecordsRepository readingRecordsRepository;

    @Autowired
    public ReadingRecordFactory(ReadingRecordService readingRecordService, DateFactory dateFactory, ReadingRecordsRepository readingRecordsRepository) {
        this.readingRecordService = readingRecordService;
        this.dateFactory = dateFactory;
        this.readingRecordsRepository = readingRecordsRepository;
    }

    public ReadingRecordImpl createReadingRecord(
            @NonNull Long bookId,
            @NonNull BookStatusRecord bookStatusRecord,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    ){

        Long readingRecordId = readingRecordService.getNextId();

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
                readingRecordsRepository
        );

        readingRecordsRepository.create(readingRecord.toDTO());

        return readingRecord;
    }

    public ReadingRecord fromDTO(@NonNull ReadingRecordDTO readingRecordDTO){
        return new ReadingRecordImpl(
                readingRecordDTO.recordId(),
                readingRecordDTO.bookId(),
                readingRecordDTO.bookStatus(),
                readingRecordDTO.startDate(),
                readingRecordDTO.endDate(),
                readingRecordDTO.isMigrated(),
                readingRecordDTO.lastChapter(),
                readingRecordsRepository
        );
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

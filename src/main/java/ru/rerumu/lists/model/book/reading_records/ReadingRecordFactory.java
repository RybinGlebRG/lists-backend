package ru.rerumu.lists.model.book.reading_records;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.repository.ReadingRecordsRepository;
import ru.rerumu.lists.services.ReadingRecordService;

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

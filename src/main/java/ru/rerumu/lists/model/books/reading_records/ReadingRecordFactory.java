package ru.rerumu.lists.model.books.reading_records;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.services.ReadingRecordService;

import java.time.LocalDateTime;

@Component
public class ReadingRecordFactory {

    private final ReadingRecordService readingRecordService;
    private final DateFactory dateFactory;

    @Autowired
    public ReadingRecordFactory(ReadingRecordService readingRecordService, DateFactory dateFactory) {
        this.readingRecordService = readingRecordService;
        this.dateFactory = dateFactory;
    }

    public ReadingRecord createReadingRecord(
            @NonNull Long bookId,
            @NonNull BookStatusRecord bookStatusRecord,
            LocalDateTime startDate,
            LocalDateTime endDate
    ){

        Long readingRecordId = readingRecordService.getNextId();

        if (startDate == null) {
            startDate = dateFactory.getLocalDateTime();
        }

        ReadingRecord readingRecord = ReadingRecord.builder()
                .bookId(bookId)
                .recordId(readingRecordId)
                .bookStatus(bookStatusRecord)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        readingRecordService.addRecord(readingRecord);

        return readingRecord;
    }
}

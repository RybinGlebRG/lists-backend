package ru.rerumu.lists.domain.readingrecord.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.utils.DateFactory;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordMyBatisEntity;
import ru.rerumu.lists.domain.readingrecord.ReadingRecord;
import ru.rerumu.lists.domain.readingrecordstatus.ReadingRecordStatuses;

import java.time.LocalDateTime;

@Component
public class ReadingRecordFactory {

    private final DateFactory dateFactory;

    @Autowired
    public ReadingRecordFactory(
            DateFactory dateFactory
    ) {
        this.dateFactory = dateFactory;
    }

    /**
     * Create new reading record
     */
    public ReadingRecord createReadingRecord(
            @NonNull Long readingRecordId,
            @NonNull Long bookId,
            @NonNull ReadingRecordStatuses bookStatusRecord,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    ){

        if (startDate == null) {
            startDate = dateFactory.getLocalDateTime();
        }

        return new ReadingRecordImpl(
                readingRecordId,
                bookId,
                bookStatusRecord,
                startDate,
                endDate,
                false,
                lastChapter,
                dateFactory,
                dateFactory.getLocalDateTime()
        );
    }

    public ReadingRecord fromDTO(@NonNull ReadingRecordMyBatisEntity readingRecordMyBatisEntity){

        LocalDateTime updateDate = readingRecordMyBatisEntity.getUpdateDate();
        if (updateDate == null) {
            updateDate = readingRecordMyBatisEntity.getStartDate();
        }

        ReadingRecordImpl readingRecord = new ReadingRecordImpl(
                readingRecordMyBatisEntity.getRecordId(),
                readingRecordMyBatisEntity.getBookId(),
                readingRecordMyBatisEntity.getBookStatus(),
                readingRecordMyBatisEntity.getStartDate(),
                readingRecordMyBatisEntity.getEndDate(),
                readingRecordMyBatisEntity.getIsMigrated(),
                readingRecordMyBatisEntity.getLastChapter(),
                dateFactory,
                updateDate
        );

        return readingRecord;
    }
}

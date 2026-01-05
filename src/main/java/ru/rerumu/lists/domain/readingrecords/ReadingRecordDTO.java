package ru.rerumu.lists.domain.readingrecords;

import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.dao.base.MyBatisEntity;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;
import ru.rerumu.lists.domain.base.EntityDTO;

import java.time.LocalDateTime;

public record ReadingRecordDTO(
        Long recordId,
        Long bookId,
        BookStatusRecord bookStatus,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Boolean isMigrated,
        Long lastChapter,
        LocalDateTime updateDate
)   implements EntityDTO<ReadingRecordDTO>, MyBatisEntity {

    @Override
    public ReadingRecordDTO toDomain() {
        throw new NotImplementedException();
    }
}

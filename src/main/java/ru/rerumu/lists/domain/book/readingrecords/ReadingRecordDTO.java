package ru.rerumu.lists.domain.book.readingrecords;

import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.domain.book.readingrecords.status.BookStatusRecord;
import ru.rerumu.lists.domain.base.EntityDTO;

import java.time.LocalDateTime;

public record ReadingRecordDTO(
        Long recordId,
        Long bookId,
        BookStatusRecord bookStatus,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Boolean isMigrated,
        Long lastChapter
)   implements EntityDTO<ReadingRecordDTO>  {

    @Override
    public ReadingRecordDTO toDomain() {
        throw new NotImplementedException();
    }
}

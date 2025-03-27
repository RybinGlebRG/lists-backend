package ru.rerumu.lists.model.book.readingrecords;

import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.model.book.readingrecords.status.BookStatusRecord;
import ru.rerumu.lists.model.base.EntityDTO;

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

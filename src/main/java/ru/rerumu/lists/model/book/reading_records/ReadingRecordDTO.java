package ru.rerumu.lists.model.book.reading_records;

import ru.rerumu.lists.exception.NotImplementedException;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.dto.EntityDTO;

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

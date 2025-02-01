package ru.rerumu.lists.mappers;

import ru.rerumu.lists.model.book.reading_records.ReadingRecordDTO;
import ru.rerumu.lists.model.book.reading_records.ReadingRecordImpl;

import java.util.List;

public interface ReadingRecordMapper extends CrudMapper<ReadingRecordDTO,Long, ReadingRecordDTO>{

    List<ReadingRecordDTO> findByBookId(Long bookId);
    List<ReadingRecordDTO> findByBookIds(List<Long> bookIds);

    Long getNextId();
}

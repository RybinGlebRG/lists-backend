package ru.rerumu.lists.repository;

import ru.rerumu.lists.model.book.reading_records.ReadingRecordDTO;
import ru.rerumu.lists.model.book.reading_records.ReadingRecordImpl;

import java.util.List;

public interface ReadingRecordsRepository extends CrudRepository<ReadingRecordDTO,Long> {
    List<ReadingRecordDTO> findByBookId(Long bookId);
    List<ReadingRecordDTO> findByBookIds(List<Long> bookIds);
}

package ru.rerumu.lists.dao.book.readingrecord;

import ru.rerumu.lists.domain.readingrecords.ReadingRecordDTO;
import ru.rerumu.lists.dao.base.CrudRepository;

import java.util.List;

public interface ReadingRecordsRepository extends CrudRepository<ReadingRecordDTO,Long> {
    List<ReadingRecordDTO> findByBookId(Long bookId);
    List<ReadingRecordDTO> findByBookIds(List<Long> bookIds);
    void delete(Long readingRecordId);
}

package ru.rerumu.lists.dao.readingrecord;

import ru.rerumu.lists.domain.readingrecords.ReadingRecordDTO;
import ru.rerumu.lists.dao.base.CrudRepository;

import java.util.List;

public interface ReadingRecordsRepository extends CrudRepository<ReadingRecordMyBatisEntity,Long> {
    List<ReadingRecordMyBatisEntity> findByBookId(Long bookId);
    List<ReadingRecordMyBatisEntity> findByBookIds(List<Long> bookIds);
    void delete(Long readingRecordId);
}

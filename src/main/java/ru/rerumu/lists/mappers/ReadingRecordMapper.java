package ru.rerumu.lists.mappers;

import ru.rerumu.lists.model.books.reading_records.ReadingRecord;

import java.util.List;

public interface ReadingRecordMapper extends CrudMapper<ReadingRecord,Long,ReadingRecord>{

    List<ReadingRecord> findByBookId(Long bookId);
    List<ReadingRecord> findByBookIds(List<Long> bookIds);

    Long getNextId();
}

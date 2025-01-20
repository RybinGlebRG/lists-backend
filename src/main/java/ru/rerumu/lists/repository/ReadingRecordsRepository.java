package ru.rerumu.lists.repository;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;

import java.util.List;

public interface ReadingRecordsRepository extends CrudRepository<ReadingRecord,Long> {
    List<ReadingRecord> findByBookId(Long bookId);
    List<ReadingRecord> findByBookIds(List<Long> bookIds);
}

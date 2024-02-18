package ru.rerumu.lists.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.rerumu.lists.model.Game;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;
import ru.rerumu.lists.repository.CrudRepository;
import ru.rerumu.lists.repository.ReadingRecordsRepository;
import ru.rerumu.lists.views.ReadingRecordAddView;
import ru.rerumu.lists.views.ReadingRecordUpdateView;

import java.util.List;

@Service
public class ReadingRecordServiceImpl implements ReadingRecordService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReadingRecordsRepository crudRepository;

    public ReadingRecordServiceImpl(ReadingRecordsRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public void addRecord(Long bookId, ReadingRecordAddView readingRecordAddView) {
        throw new RuntimeException();
    }

    @Override
    public void updateRecord(Long recordId, ReadingRecordUpdateView readingRecordUpdateView) {
        throw new RuntimeException();
    }

    @Override
    public void deleteRecord(Long recordId) {
        throw new RuntimeException();
    }

    @Override
    public List<ReadingRecord> getReadingRecords(Long bookId) {
        return crudRepository.findByBookId(bookId);
    }
}

package ru.rerumu.lists.repository.impl;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.mappers.CrudMapper;
import ru.rerumu.lists.mappers.ReadingRecordMapper;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;
import ru.rerumu.lists.repository.ReadingRecordsRepository;

import java.util.List;
import java.util.Optional;

@Component
public class ReadingRecordRepositoryImpl extends CrudRepositoryEntityImpl<ReadingRecord,Long> implements ReadingRecordsRepository {

    private final ReadingRecordMapper mapper;

    public ReadingRecordRepositoryImpl(ReadingRecordMapper mapper) {
        super(mapper);
        this.mapper = mapper;
    }

    @Override
    public List<ReadingRecord> findByBookId(Long bookId) {
        return mapper.findByBookId(bookId);
    }

    @Override
    public Long getNextId() {
        return mapper.getNextId();
    }
}

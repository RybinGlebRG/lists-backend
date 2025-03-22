package ru.rerumu.lists.dao.readingrecord.impl;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.mappers.ReadingRecordMapper;
import ru.rerumu.lists.model.book.readingrecords.ReadingRecordDTO;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.dao.base.impl.CrudRepositoryDtoImpl;

import java.util.List;

@Component
public class ReadingRecordRepositoryImpl extends CrudRepositoryDtoImpl<ReadingRecordDTO,Long> implements ReadingRecordsRepository {

    private final ReadingRecordMapper mapper;

    public ReadingRecordRepositoryImpl(ReadingRecordMapper mapper) {
        super(mapper);
        this.mapper = mapper;
    }

    @Override
    public List<ReadingRecordDTO> findByBookId(Long bookId) {
        return mapper.findByBookId(bookId);
    }

    @Override
    public List<ReadingRecordDTO> findByBookIds(List<Long> bookIds) {
        return mapper.findByBookIds(bookIds);
    }

    @Override
    public Long getNextId() {
        return mapper.getNextId();
    }
}

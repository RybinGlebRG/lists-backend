package ru.rerumu.lists.dao.readingrecord.impl;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.UnsupportedMethodException;
import ru.rerumu.lists.dao.base.impl.CrudRepositoryDtoImpl;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordMyBatisEntity;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.dao.readingrecord.mapper.ReadingRecordMapperMyBatis;

import java.util.List;


@Component
public class ReadingRecordRepositoryMyBatis extends CrudRepositoryDtoImpl<ReadingRecordMyBatisEntity,Long> implements ReadingRecordsRepository {

    private final ReadingRecordMapperMyBatis mapper;

    public ReadingRecordRepositoryMyBatis(ReadingRecordMapperMyBatis mapper) {
        super(mapper);
        this.mapper = mapper;
    }

    @Override
    public List<ReadingRecordMyBatisEntity> findByBookId(Long bookId) {
        return mapper.findByBookId(bookId);
    }

    @Override
    public List<ReadingRecordMyBatisEntity> findByBookIds(List<Long> bookIds) {
        return mapper.findByBookIds(bookIds);
    }

    @Override
    public void delete(Long readingRecordId) {
        mapper.delete(readingRecordId);
    }

    @Override
    public void save(ReadingRecordMyBatisEntity entity) {
        throw new UnsupportedMethodException();
    }

    @Override
    public Long getNextId() {
        return mapper.getNextId();
    }
}

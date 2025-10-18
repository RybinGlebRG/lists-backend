package ru.rerumu.lists.dao.book.readingrecord.impl;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.UnsupportedMethodException;
import ru.rerumu.lists.dao.base.impl.CrudRepositoryDtoImpl;
import ru.rerumu.lists.dao.book.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.dao.book.readingrecord.mapper.ReadingRecordMapper;
import ru.rerumu.lists.domain.readingrecords.ReadingRecordDTO;
import ru.rerumu.lists.domain.user.User;

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
    public void delete(Long readingRecordId) {
        mapper.delete(readingRecordId);
    }

    @Override
    public void delete(Long aLong, User user) {
        throw new UnsupportedMethodException();
    }

    @Override
    public Long getNextId() {
        return mapper.getNextId();
    }
}

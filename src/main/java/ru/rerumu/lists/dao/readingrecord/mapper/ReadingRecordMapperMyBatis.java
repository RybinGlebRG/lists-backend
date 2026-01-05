package ru.rerumu.lists.dao.readingrecord.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordMyBatisEntity;
import ru.rerumu.lists.domain.readingrecords.ReadingRecordDTO;

import java.util.List;

@Mapper
public interface ReadingRecordMapperMyBatis extends CrudMapper<ReadingRecordMyBatisEntity,Long, ReadingRecordMyBatisEntity> {

    List<ReadingRecordMyBatisEntity> findByBookId(Long bookId);
    List<ReadingRecordMyBatisEntity> findByBookIds(List<Long> bookIds);

    Long getNextId();

    void delete(Long readingRecordId);
}

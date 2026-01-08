package ru.rerumu.lists.dao.readingrecord.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordMyBatisEntity;

import java.util.List;

@Mapper
public interface ReadingRecordMapper extends CrudMapper<ReadingRecordMyBatisEntity,Long, ReadingRecordMyBatisEntity> {

    List<ReadingRecordMyBatisEntity> findByBookId(Long bookId);
    List<ReadingRecordMyBatisEntity> findByBookIds(List<Long> bookIds);

    void delete(Long readingRecordId);
}

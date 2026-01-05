package ru.rerumu.lists.dao.readingrecord.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.domain.readingrecords.ReadingRecordDTO;

import java.util.List;

@Mapper
public interface ReadingRecordMapper extends CrudMapper<ReadingRecordDTO,Long, ReadingRecordDTO> {

    List<ReadingRecordDTO> findByBookId(Long bookId);
    List<ReadingRecordDTO> findByBookIds(List<Long> bookIds);

    Long getNextId();

    void delete(Long readingRecordId);
}

package ru.rerumu.lists.dao.book.readingrecord.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.model.book.readingrecords.ReadingRecordDTO;

import java.util.List;

@Mapper
public interface ReadingRecordMapper extends CrudMapper<ReadingRecordDTO,Long, ReadingRecordDTO> {

    List<ReadingRecordDTO> findByBookId(Long bookId);
    List<ReadingRecordDTO> findByBookIds(List<Long> bookIds);

    Long getNextId();
}

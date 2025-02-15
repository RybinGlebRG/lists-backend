package ru.rerumu.lists.mappers;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.model.book.reading_records.ReadingRecordDTO;

import java.util.List;

@Mapper
public interface ReadingRecordMapper extends CrudMapper<ReadingRecordDTO,Long, ReadingRecordDTO>{

    List<ReadingRecordDTO> findByBookId(Long bookId);
    List<ReadingRecordDTO> findByBookIds(List<Long> bookIds);

    Long getNextId();
}

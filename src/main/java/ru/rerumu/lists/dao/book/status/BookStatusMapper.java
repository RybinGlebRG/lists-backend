package ru.rerumu.lists.dao.book.status;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.model.book.readingrecords.status.BookStatusRecord;

@Mapper
public interface BookStatusMapper extends CrudMapper<BookStatusRecord,Integer,BookStatusRecord> {
}

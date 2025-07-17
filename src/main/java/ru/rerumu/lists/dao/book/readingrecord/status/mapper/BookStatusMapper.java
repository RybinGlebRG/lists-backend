package ru.rerumu.lists.dao.book.readingrecord.status.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.domain.book.readingrecords.status.BookStatusRecord;

@Mapper
public interface BookStatusMapper extends CrudMapper<BookStatusRecord,Integer,BookStatusRecord> {
}

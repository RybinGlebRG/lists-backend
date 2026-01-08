package ru.rerumu.lists.dao.readingrecordstatus.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;

@Mapper
public interface BookStatusMapper extends CrudMapper<BookStatusRecord,Integer,BookStatusRecord> {
}

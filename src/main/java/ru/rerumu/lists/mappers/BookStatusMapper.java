package ru.rerumu.lists.mappers;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.model.BookStatusRecord;

@Mapper
public interface BookStatusMapper extends CrudMapper<BookStatusRecord,Integer,BookStatusRecord>{
}

package ru.rerumu.lists.dao.booktype.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.base.CrudDictionaryMapper;
import ru.rerumu.lists.domain.booktype.BookType;

@Mapper
public interface BookTypeMapper extends CrudDictionaryMapper<BookType, Long> {

}

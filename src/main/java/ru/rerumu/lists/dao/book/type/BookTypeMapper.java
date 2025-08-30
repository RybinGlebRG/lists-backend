package ru.rerumu.lists.dao.book.type;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.domain.book.type.BookTypeDTO;

@Mapper
public interface BookTypeMapper extends CrudMapper<BookTypeDTO, Integer, BookTypeDTO> {

}

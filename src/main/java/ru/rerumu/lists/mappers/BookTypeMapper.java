package ru.rerumu.lists.mappers;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.model.book.type.BookTypeDTO;

@Mapper
public interface BookTypeMapper extends CrudMapper<BookTypeDTO, Integer, BookTypeDTO> {

}

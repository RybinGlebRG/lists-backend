package ru.rerumu.lists.mappers;

import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.model.dto.BookTypeDTO;
import ru.rerumu.lists.model.dto.EntityDTO;

import java.util.ArrayList;
import java.util.List;

public interface BookTypeMapper extends CrudMapper<BookType, Integer, EntityDTO<BookType>> {

}

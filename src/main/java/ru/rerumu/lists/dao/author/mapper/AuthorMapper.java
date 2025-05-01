package ru.rerumu.lists.dao.author.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.author.AuthorDtoDao;
import ru.rerumu.lists.dao.base.CrudMapper;

@Mapper
public interface AuthorMapper extends CrudMapper<AuthorDtoDao, Long, AuthorDtoDao> {
}

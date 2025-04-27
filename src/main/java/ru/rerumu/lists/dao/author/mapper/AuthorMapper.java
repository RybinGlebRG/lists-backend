package ru.rerumu.lists.dao.author.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.author.AuthorDtoDao;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.model.author.impl.AuthorImpl;

@Mapper
public interface AuthorMapper extends CrudMapper<AuthorDtoDao, Long, AuthorDtoDao> {
}

package ru.rerumu.lists.dao.tag;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookTagMapper{

    void delete(Long bookId, Long tagId);

    // TODO
    void add(Long bookId, long tagId);
}

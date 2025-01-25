package ru.rerumu.lists.dao.author;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.model.Author;

import java.util.List;

@Mapper
public interface AuthorMapper {
    Author getOne(Long readListId, Long authorId);
    List<Author> getAll(Long readListId);

    void addOne(Long readListId,Long authorId, String name);
    Long getNextId();

    void deleteOne(Long authorId);
}

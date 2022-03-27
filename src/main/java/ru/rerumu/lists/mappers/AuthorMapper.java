package ru.rerumu.lists.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Bean;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.Series;

public interface AuthorMapper {
    Author getOne(Long readListId, Long authorId);
}

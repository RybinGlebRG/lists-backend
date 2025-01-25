package ru.rerumu.lists.mappers;

import ru.rerumu.lists.model.Author;

import java.util.List;

public interface AuthorMapper {
    Author getOne(Long readListId, Long authorId);
    List<Author> getAll(Long readListId);

    void addOne(Long readListId,Long authorId, String name);
    Long getNextId();

    void deleteOne(Long authorId);
}

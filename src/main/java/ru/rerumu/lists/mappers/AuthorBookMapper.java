package ru.rerumu.lists.mappers;

import ru.rerumu.lists.model.AuthorBookRelation;

import java.util.List;

public interface AuthorBookMapper {

    void deleteByAuthor(Long authorId);

    void add(Long bookId, Long authorId, Long readListId);

    List<AuthorBookRelation> getByBookId(Long bookId);
}

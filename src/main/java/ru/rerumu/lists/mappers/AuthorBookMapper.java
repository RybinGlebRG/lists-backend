package ru.rerumu.lists.mappers;

public interface AuthorBookMapper {

    void deleteByAuthor(Long authorId);

    void add(Long bookId, Long authorId, Long readListId);
}

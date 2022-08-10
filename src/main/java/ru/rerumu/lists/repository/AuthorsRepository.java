package ru.rerumu.lists.repository;

import ru.rerumu.lists.model.Author;

import java.util.List;

public interface AuthorsRepository {

    Author getOne(Long readListId, Long authorId);
    List<Author> getAll(Long readListId);

    void addOne(Author author);

    Long getNextId();
}

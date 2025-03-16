package ru.rerumu.lists.dao.author;

import ru.rerumu.lists.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorsRepository {

    Optional<Author> getOne(Long readListId, Long authorId);
    List<Author> getAll(Long readListId);

    void addOne(Author author);

    Long getNextId();

    void deleteOne(Long authorId);
}

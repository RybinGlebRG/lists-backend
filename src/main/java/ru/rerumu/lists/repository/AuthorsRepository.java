package ru.rerumu.lists.repository;

import ru.rerumu.lists.model.Author;

public interface AuthorsRepository {

    Author getOne(Long readListId, Long authorId);
}

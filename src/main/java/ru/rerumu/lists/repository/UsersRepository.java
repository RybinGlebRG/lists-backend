package ru.rerumu.lists.repository;

import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.TokenRequest;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.repository.impl.CrudRepositoryEntityImpl;

public interface UsersRepository {

    User getOne(String name);

    boolean isOwner(String name, Long listId);
    boolean isOwnerAuthor(String name, Long authorId);

    boolean isOwnerBook(String name, Long bookId);
    boolean isOwnerSeries(String name, Long seriesId);
}

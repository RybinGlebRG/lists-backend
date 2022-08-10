package ru.rerumu.lists.repository;

import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.TokenRequest;
import ru.rerumu.lists.model.User;

public interface UsersRepository {

    User getOne(String name);

    boolean isOwner(String name, Long listId);
}

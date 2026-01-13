package ru.rerumu.lists.domain.author;

import ru.rerumu.lists.domain.user.User;

public interface Author {

    Long getId();

    String getName();

    User getUser();
}

package ru.rerumu.lists.domain.tag;

import ru.rerumu.lists.domain.user.User;

public interface Tag {

    Long getId();
    String getName();
    User getUser();

}

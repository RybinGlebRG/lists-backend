package ru.rerumu.lists.model.base;

import ru.rerumu.lists.model.user.User;

public interface Entity {

    /**
     * Get entity id
     */
    Long getId();

    /**
     * Get user that entity belongs to
     */
    User getUser();

}

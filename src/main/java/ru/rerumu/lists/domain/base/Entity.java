package ru.rerumu.lists.domain.base;

import ru.rerumu.lists.domain.user.User;

public interface Entity {

    /**
     * Get entity id
     */
    Long getId();

    /**
     * Get user that entity belongs to
     */
    User getUser();

//    /**
//     * Save entity
//     */
//    @Deprecated
//    void save();
//
//    /**
//     * Delete the entity
//     */
//    @Deprecated
//    void delete();

}

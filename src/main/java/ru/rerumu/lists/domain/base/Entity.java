package ru.rerumu.lists.domain.base;

import ru.rerumu.lists.domain.user.User;

public interface Entity<T> {

    /**
     * Get entity id
     */
    Long getId();

    /**
     * Get user that entity belongs to
     */
    User getUser();

    /**
     * Save entity
     */
    void save();

    /**
     * Delete the entity
     */
    void delete();

//    /**
//     * Get entity state
//     */
//    EntityState getEntityState();
//
//    /**
//     * Get copy of the object corresponding to its state in DB
//     */
//    T getPersistedCopy();

}

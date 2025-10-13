package ru.rerumu.lists.domain.base;

import lombok.Getter;
import lombok.ToString;

@ToString(doNotUseGetters = true)
public abstract class EntityBaseImpl<T> {

    protected EntityState entityState;

    /**
     * Copy of object corresponding to its state in DB
     */
    @Getter
    protected T persistentCopy;

    protected EntityBaseImpl(EntityState entityState) {
        this.entityState = entityState;
    }

    protected abstract void initPersistentCopy();
}

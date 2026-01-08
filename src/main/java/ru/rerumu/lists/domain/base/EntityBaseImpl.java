package ru.rerumu.lists.domain.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(doNotUseGetters = true)
public abstract class EntityBaseImpl<T> {

    @Getter
    @Setter
    protected EntityState entityState;

    /**
     * Copy of the object corresponding to its state in DB
     */
    @Getter
    protected T persistedCopy;

    protected EntityBaseImpl(EntityState entityState) {
        this.entityState = entityState;
    }

    protected abstract void initPersistentCopy();
}

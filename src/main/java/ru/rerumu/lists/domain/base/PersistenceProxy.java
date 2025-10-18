package ru.rerumu.lists.domain.base;

import lombok.Getter;
import lombok.Setter;

public abstract class PersistenceProxy<T> {

    @Getter
    protected T persistedCopy;

    @Getter
    @Setter
    protected EntityState entityState;

    public PersistenceProxy(EntityState entityState) {
        this.entityState = entityState;
    }

    public abstract void initPersistedCopy();

    public abstract void clearPersistedCopy();

}

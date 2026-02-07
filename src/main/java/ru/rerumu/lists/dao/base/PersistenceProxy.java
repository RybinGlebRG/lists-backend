package ru.rerumu.lists.dao.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(doNotUseGetters = true)
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

    public void onSave() {
        entityState = EntityState.PERSISTED;
        initPersistedCopy();
    }

}

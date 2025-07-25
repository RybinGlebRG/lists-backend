package ru.rerumu.lists.domain.base;

public class EntityBaseImpl {

    protected EntityState entityState;

    protected EntityBaseImpl(EntityState entityState) {
        this.entityState = entityState;
    }
}

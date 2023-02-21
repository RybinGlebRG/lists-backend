package ru.rerumu.lists.repository;

import ru.rerumu.lists.model.User;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T,ID> {

    Optional<T> findById(ID id);

    @Deprecated
    List<T> findAll();
    List<T> findAll(User user);
    void save(T entity);
}

package ru.rerumu.lists.repository;

import ru.rerumu.lists.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T,ID> {

    Optional<T> findById(ID id);

    @Deprecated
    List<T> findAll();
    default List<T> findAll(User user){
        return new ArrayList<>();
    };
    void save(T entity);
}

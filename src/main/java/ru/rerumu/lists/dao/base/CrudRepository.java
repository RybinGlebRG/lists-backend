package ru.rerumu.lists.dao.base;

import ru.rerumu.lists.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T,ID> {

    Optional<T> findById(ID id, User user);

    List<T> findAll();
    List<T> findByUser(User user);
    List<T> findByUserId(Long userId);

    @Deprecated
    void save(T entity);

    void create(T entity);

    void update(T entity);
    ID getNextId();

    void delete(ID id, User user);
}

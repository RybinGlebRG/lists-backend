package ru.rerumu.lists.dao.base;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.domain.user.User;

import java.util.List;

@Component("CrudMapper")
public interface CrudMapper<T,ID, R> {

    R findById(ID id, Long userId);

    List<R> findAll();

    List<R> findByUser(User user);

    List<R> findByUserId(Long userId);

    List<R> findByIds(List<ID> ids, Long userId);

    void save(T entity);

    void create(T entity);

    void update(T entity);

    @Deprecated
    ID nextval();

    ID getNextId();

    void delete(ID id, Long userId);
}

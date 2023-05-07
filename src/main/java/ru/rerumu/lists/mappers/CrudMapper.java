package ru.rerumu.lists.mappers;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.model.User;

import java.util.List;

@Component("CrudMapper")
public interface CrudMapper<T,ID, R> {

    R findById(ID id);


    List<R> findAll();

    List<R> findByUser(User user);

    void save(T entity);
}

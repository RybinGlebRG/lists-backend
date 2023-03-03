package ru.rerumu.lists.mappers;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.model.User;

import java.util.ArrayList;
import java.util.List;

@Component("CrudMapper")
public interface CrudMapper<T,ID, R> {

    R findById(ID id);

    @Deprecated
    default List<R> findAll(){
        return new ArrayList<>();
    };

    List<R> findAll(User user);

    void save(T entity);
}

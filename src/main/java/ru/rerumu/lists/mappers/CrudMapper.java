package ru.rerumu.lists.mappers;

import org.springframework.stereotype.Component;

import java.util.List;

@Component("CrudMapper")
public interface CrudMapper<T,ID, R> {

    R findById(ID id);

    List<R> findAll();

    void save(T entity);
}

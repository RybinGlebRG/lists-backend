package ru.rerumu.lists.mappers;

import org.springframework.stereotype.Component;

import java.util.List;

@Component("CrudMapper")
public interface CrudMapper<T,ID> {

    T findById(ID id);

    List<T> findAll();
}

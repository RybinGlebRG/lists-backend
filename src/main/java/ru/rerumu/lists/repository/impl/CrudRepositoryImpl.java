package ru.rerumu.lists.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.mappers.CrudMapper;
import ru.rerumu.lists.repository.CrudRepository;

@Component
public abstract class CrudRepositoryImpl<T,ID,R> implements CrudRepository<T,ID> {


    private final CrudMapper<T,ID, R> mapper;

    public CrudRepositoryImpl(@Qualifier("CrudMapper") CrudMapper<T,ID, R> mapper) {
        this.mapper = mapper;
    }

//    @Override
//    public Optional<T> findById(ID id) {
//        return Optional.ofNullable(mapper.findById(id));
//    }

//    @Override
//    public List<T> findAll() {
//        return mapper.findAll();
//    }

    @Override
    public void save(T entity) {
        throw new RuntimeException("Not Ready");
    }
}

package ru.rerumu.lists.repository.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.mappers.CrudMapper;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

//@Component("CrudRepositoryEntityImpl")
public class CrudRepositoryEntityImpl<T,ID> implements CrudRepository<T,ID> {


    protected final CrudMapper<T,ID, T> mapper;

    public CrudRepositoryEntityImpl(
//            @Qualifier("CrudMapper")
            CrudMapper<T,ID, T> mapper
    ) {
        this.mapper = mapper;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(mapper.findById(id));
    }


    @Override
    public List<T> findAll() {
        return mapper.findAll();
    }

    @Override
    public List<T> findByUser(User user) {
        return mapper.findByUser(user);
    }

    @Override
    public void save(T entity) {
        mapper.save(entity);
    }
}

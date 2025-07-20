package ru.rerumu.lists.dao.base.impl;

import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.dao.base.CrudRepository;
import ru.rerumu.lists.domain.user.User;

import java.util.List;
import java.util.Optional;

public class CrudRepositoryDictionaryImpl<T,ID> implements CrudRepository<T,ID> {

    protected final CrudMapper<T,ID, T> mapper;

    public CrudRepositoryDictionaryImpl(CrudMapper<T, ID, T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<T> findById(ID id, User user) {
        return Optional.ofNullable(mapper.findById(id, null));
    }

    @Override
    public List<T> findAll() {
        return mapper.findAll();
    }

    @Override
    public List<T> findByUser(User user) {
        throw new NotImplementedException();
    }

    @Override
    public void save(T entity) {
        throw new NotImplementedException();
    }

    @Override
    public void create(T entity) {
        throw new NotImplementedException();
    }

    @Override
    public void update(T entity) {
        throw new NotImplementedException();
    }

    @Override
    public ID getNextId() {
        throw new NotImplementedException();
    }

    @Override
    public void delete(ID id, User user) {
        throw new NotImplementedException();
    }
}

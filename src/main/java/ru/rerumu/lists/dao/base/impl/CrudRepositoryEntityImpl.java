package ru.rerumu.lists.dao.base.impl;

import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.dao.base.CrudRepository;

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
    public Optional<T> findById(ID id, User user) {
        return Optional.ofNullable(mapper.findById(id, user.userId()));
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

    @Override
    public void create(T entity) {
        mapper.create(entity);
    }

    @Override
    public void update(T entity) {
        mapper.update(entity);
    }

    @Override
    public ID getNextId() {
        return mapper.nextval();
    }

    @Override
    public void delete(ID id, User user) {
        mapper.delete(id, user.userId());
    }
}

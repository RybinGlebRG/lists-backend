package ru.rerumu.lists.dao.base.impl;

import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.base.EntityDTO;
import ru.rerumu.lists.dao.base.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CrudRepositoryDtoImpl<T extends EntityDTO<?>,ID> implements CrudRepository<T,ID> {

    private final CrudMapper<T, ID, T> mapper;

    public CrudRepositoryDtoImpl(CrudMapper<T, ID, T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<T> findById(ID id, User user){
        T entityDTO = mapper.findById(id, user.userId());
        return Optional.ofNullable(entityDTO);
    }

    @Override
    public List<T> findAll() {
        List<T> entityDTOList = mapper.findAll();
        return entityDTOList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<T> findByUser(User user){
        List<T> entityDTOList = mapper.findByUser(user);
        return entityDTOList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
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

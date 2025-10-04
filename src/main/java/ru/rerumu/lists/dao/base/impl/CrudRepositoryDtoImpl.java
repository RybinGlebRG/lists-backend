package ru.rerumu.lists.dao.base.impl;

import com.jcabi.aspects.Loggable;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.dao.base.CrudRepository;
import ru.rerumu.lists.dao.base.EntityDTOv2;
import ru.rerumu.lists.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CrudRepositoryDtoImpl<T extends EntityDTOv2,ID> implements CrudRepository<T,ID> {

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
    @Loggable(value = Loggable.DEBUG, prepend = true, trim = false)
    public List<T> findByUser(User user){
        List<T> entityDTOList = mapper.findByUser(user);
        return entityDTOList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<T> findByUserId(Long userId) {
        return mapper.findByUserId(userId);
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

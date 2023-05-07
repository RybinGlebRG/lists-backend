package ru.rerumu.lists.repository.impl;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.rerumu.lists.mappers.CrudMapper;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.model.dto.EntityDTO;
import ru.rerumu.lists.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CrudRepositoryDtoImpl<T,ID> implements CrudRepository<T,ID> {

    private final CrudMapper<T,ID, EntityDTO<T>> mapper;

    public CrudRepositoryDtoImpl(CrudMapper<T,ID, EntityDTO<T>> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<T> findById(ID id){
        EntityDTO<T> entityDTO = mapper.findById(id);
        if (entityDTO==null){
            return Optional.empty();
        } else {
            return Optional.ofNullable(entityDTO.toDomain());
        }
    }

    @Override
    public List<T> findAll() {
        List<EntityDTO<T>> entityDTOList = mapper.findAll();
        return entityDTOList.stream()
                .map(EntityDTO::toDomain)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<T> findByUser(User user){
        List<EntityDTO<T>> entityDTOList = mapper.findByUser(user);
        return entityDTOList.stream()
                .map(EntityDTO::toDomain)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void save(T entity) {
        throw new RuntimeException("Not Ready");
    }
}

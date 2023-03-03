package ru.rerumu.lists.repository.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.rerumu.lists.mappers.CrudMapper;
import ru.rerumu.lists.model.dto.EntityDTO;
import ru.rerumu.lists.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class CrudRepositoryDtoImpl<T,ID> implements CrudRepository<T,ID> {

    private final CrudMapper<T,ID, EntityDTO<T>> mapper;

    public CrudRepositoryDtoImpl(@Qualifier("CrudMapper") CrudMapper<T,ID, EntityDTO<T>> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<T> findById(ID id){
        EntityDTO<T> entityDTO = mapper.findById(id);
        if (entityDTO==null){
            return Optional.empty();
        } else {
            return Optional.of(entityDTO.toDomain());
        }
    }

    @Override
    public List<T> findAll() {
        List<EntityDTO<T>> entityDTOList = mapper.findAll();
        return entityDTOList.stream()
                .map(EntityDTO::toDomain)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void save(T entity) {
        throw new RuntimeException("Not Ready");
    }
}

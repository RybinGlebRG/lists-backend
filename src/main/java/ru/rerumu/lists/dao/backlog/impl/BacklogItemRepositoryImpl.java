package ru.rerumu.lists.dao.backlog.impl;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.backlog.BacklogItemDTO;
import ru.rerumu.lists.dao.backlog.BacklogItemRepository;
import ru.rerumu.lists.dao.backlog.mapper.BacklogItemMapper;
import ru.rerumu.lists.dao.base.impl.CrudRepositoryDtoImpl;

@Component
public class BacklogItemRepositoryImpl extends CrudRepositoryDtoImpl<BacklogItemDTO, Long> implements BacklogItemRepository {

    private final BacklogItemMapper backlogItemMapper;

    public BacklogItemRepositoryImpl(BacklogItemMapper backlogItemMapper) {
        super(backlogItemMapper);
        this.backlogItemMapper = backlogItemMapper;
    }
}

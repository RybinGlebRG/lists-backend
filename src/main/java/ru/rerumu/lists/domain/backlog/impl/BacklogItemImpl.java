package ru.rerumu.lists.domain.backlog.impl;

import lombok.Getter;
import ru.rerumu.lists.dao.backlog.BacklogItemDTO;
import ru.rerumu.lists.dao.backlog.BacklogItemRepository;
import ru.rerumu.lists.domain.backlog.BacklogItem;
import ru.rerumu.lists.domain.base.EntityBaseImpl;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.series.item.SeriesItemType;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;

public class BacklogItemImpl extends EntityBaseImpl implements BacklogItem {

    private final BacklogItemRepository backlogItemRepository;


    private final Long id;

    @Getter
    private final String title;

    @Getter
    private final SeriesItemType type;

    @Getter
    private final String note;

    private final User user;

    @Getter
    private final LocalDateTime creationDate;

    public BacklogItemImpl(
            Long id,
            String title,
            SeriesItemType type,
            String note,
            User user,
            LocalDateTime creationDate,
            EntityState entityState,
            BacklogItemRepository backlogItemRepository
    ) {
        super(entityState);
        this.id = id;
        this.title = title;
        this.type = type;
        this.note = note;
        this.user = user;
        this.creationDate = creationDate;
        this.backlogItemRepository = backlogItemRepository;
    }

    private BacklogItemDTO toDTO() {
        return new BacklogItemDTO(
            id,
            title,
            type.getId(),
            note,
            user.userId(),
            creationDate
        );
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void save() {
        if (entityState.equals(EntityState.NEW)) {
            backlogItemRepository.create(toDTO());
        } else if (entityState.equals(EntityState.DIRTY)) {
            backlogItemRepository.update(toDTO());
        }
    }
}

package ru.rerumu.lists.domain.backlog.impl;

import lombok.Getter;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
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
    private String title;

    @Getter
    private SeriesItemType type;

    @Getter
    private String note;

    private final User user;

    @Getter
    private LocalDateTime creationDate;

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
            user.getId(),
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

        entityState = EntityState.PERSISTED;
    }

    @Override
    public void delete() {
        backlogItemRepository.delete(id, user);
    }

    @Override
    public void updateTitle(String title) {
        this.title = title;

        entityState = EntityState.DIRTY;
    }

    @Override
    public void updateType(SeriesItemType type) {
        this.type = type;

        entityState = EntityState.DIRTY;
    }

    @Override
    public void updateNote(String note) {
        this.note = note;

        entityState = EntityState.DIRTY;
    }

    @Override
    public void updateCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;

        entityState = EntityState.DIRTY;
    }

    @Override
    protected void initPersistentCopy() {
        throw new NotImplementedException();
    }
}

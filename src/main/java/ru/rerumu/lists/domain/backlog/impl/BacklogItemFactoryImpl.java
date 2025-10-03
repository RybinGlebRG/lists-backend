package ru.rerumu.lists.domain.backlog.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.backlog.BacklogItemRepository;
import ru.rerumu.lists.domain.backlog.BacklogItem;
import ru.rerumu.lists.domain.backlog.BacklogItemFactory;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.series.item.SeriesItemType;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class BacklogItemFactoryImpl implements BacklogItemFactory {

    private final BacklogItemRepository backlogItemRepository;

    @Autowired
    public BacklogItemFactoryImpl(BacklogItemRepository backlogItemRepository) {
        this.backlogItemRepository = backlogItemRepository;
    }

    @Override
    public BacklogItem create(@NonNull String title, @NonNull SeriesItemType type, String note, @NonNull User user) {
        Long nextId = backlogItemRepository.getNextId();

        return new BacklogItemImpl(
                nextId,
                title,
                type,
                note,
                user,
                LocalDateTime.now(ZoneOffset.UTC),
                EntityState.NEW,
                backlogItemRepository
        );
    }
}

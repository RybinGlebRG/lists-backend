package ru.rerumu.lists.domain.backlog.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.backlog.BacklogItemDTO;
import ru.rerumu.lists.dao.backlog.BacklogItemRepository;
import ru.rerumu.lists.domain.backlog.BacklogItem;
import ru.rerumu.lists.domain.backlog.BacklogItemFactory;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.seriesitem.SeriesItemType;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BacklogItemFactoryImpl implements BacklogItemFactory {

    private final BacklogItemRepository backlogItemRepository;

    @Autowired
    public BacklogItemFactoryImpl(BacklogItemRepository backlogItemRepository) {
        this.backlogItemRepository = backlogItemRepository;
    }

    @Override
    public BacklogItem create(@NonNull String title, @NonNull SeriesItemType type, String note, @NonNull User user, LocalDateTime creationDate) {
        Long nextId = backlogItemRepository.getNextId();

        // Use date from request if provided
        LocalDateTime actualCreationDate;
        if (creationDate != null) {
            actualCreationDate = creationDate;
        } else {
            // Otherwise use current date
            actualCreationDate = LocalDateTime.now(ZoneOffset.UTC);
        }

        return new BacklogItemImpl(
                nextId,
                title,
                type,
                note,
                user,
                actualCreationDate,
                EntityState.NEW,
                backlogItemRepository
        );
    }

    @Override
    public List<BacklogItem> loadByUser(@NonNull User user) {
        List<BacklogItemDTO> backlogItemDTOs = backlogItemRepository.findByUserId(user.getId());

        return backlogItemDTOs.stream()
                .map(backlogItemDTO -> fromDTO(backlogItemDTO, user))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public BacklogItem loadById(@NonNull User user, @NonNull Long backlogItemId) {
        BacklogItemDTO backlogItemDTO = backlogItemRepository.findById(backlogItemId, user).orElseThrow(EntityNotFoundException::new);
        return fromDTO(backlogItemDTO, user);
    }

    @NonNull
    private BacklogItem fromDTO(@NonNull BacklogItemDTO backlogItemDTO, @NonNull User user) {
        return new BacklogItemImpl(
                backlogItemDTO.getId(),
                backlogItemDTO.getTitle(),
                SeriesItemType.findById(backlogItemDTO.getTypeId()),
                backlogItemDTO.getNote(),
                user,
                backlogItemDTO.getCreationDate(),
                EntityState.PERSISTED,
                backlogItemRepository
        );
    }
}

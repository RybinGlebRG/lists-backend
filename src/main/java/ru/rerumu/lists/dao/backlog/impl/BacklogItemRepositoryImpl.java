package ru.rerumu.lists.dao.backlog.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.backlog.BacklogItemDTO;
import ru.rerumu.lists.dao.backlog.BacklogItemRepository;
import ru.rerumu.lists.dao.backlog.mapper.BacklogItemMapper;
import ru.rerumu.lists.domain.backlog.BacklogItem;
import ru.rerumu.lists.domain.backlog.impl.BacklogItemImpl;
import ru.rerumu.lists.domain.seriesitem.SeriesItemType;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BacklogItemRepositoryImpl implements BacklogItemRepository {

    private final BacklogItemMapper backlogItemMapper;

    @Autowired
    public BacklogItemRepositoryImpl(BacklogItemMapper backlogItemMapper) {
        this.backlogItemMapper = backlogItemMapper;
    }

    @Override
    public Long getNextId() {
        return backlogItemMapper.getNextId();
    }

    @Override
    public void delete(BacklogItem backlogItem) {
        backlogItemMapper.delete(backlogItem.getId(), backlogItem.getUser().getId());
    }

    @Override
    public void save(BacklogItem backlogItem) {

        BacklogItemDTO backlogItemDTO = new BacklogItemDTO(
                backlogItem.getId(),
                backlogItem.getTitle(),
                backlogItem.getType().getId(),
                backlogItem.getNote(),
                backlogItem.getUser().getId(),
                backlogItem.getCreationDate()
        );

        backlogItemMapper.update(backlogItemDTO);
    }

    @Override
    public BacklogItem create(@NonNull String title, @NonNull SeriesItemType type, String note, @NonNull User user, LocalDateTime creationDate) {
        Long nextId = getNextId();

        // Use date from request if provided
        LocalDateTime actualCreationDate;
        if (creationDate != null) {
            actualCreationDate = creationDate;
        } else {
            // Otherwise use current date
            actualCreationDate = LocalDateTime.now(ZoneOffset.UTC);
        }

        BacklogItem backlogItem =  new BacklogItemImpl(
                nextId,
                title,
                type,
                note,
                user,
                actualCreationDate
        );

        backlogItemMapper.create(new BacklogItemDTO(
                backlogItem.getId(),
                backlogItem.getTitle(),
                backlogItem.getType().getId(),
                backlogItem.getNote(),
                backlogItem.getUser().getId(),
                backlogItem.getCreationDate()
        ));

        return backlogItem;
    }

    @Override
    @NonNull
    public List<BacklogItem> findByUser(@NonNull User user) {
        List<BacklogItemDTO> backlogItemDTOList = backlogItemMapper.findByUserId(user.getId());

        return backlogItemDTOList.stream()
                .map(backlogItemDTO -> {

                    SeriesItemType seriesItemType = SeriesItemType.findById(backlogItemDTO.getTypeId());
                    if (seriesItemType == null) {
                        throw new EntityNotFoundException();
                    }

                    return new BacklogItemImpl(
                            backlogItemDTO.getId(),
                            backlogItemDTO.getTitle(),
                            seriesItemType,
                            backlogItemDTO.getNote(),
                            user,
                            backlogItemDTO.getCreationDate()
                    );
                })
                .collect(Collectors.toCollection(ArrayList::new));

    }

    @Override
    public @NonNull BacklogItem findById(@NonNull Long id, @NonNull User user) {
        BacklogItemDTO backlogItemDTO = backlogItemMapper.findById(id, user.getId());
        if (backlogItemDTO == null) {
            throw new EntityNotFoundException();
        }

        SeriesItemType seriesItemType = SeriesItemType.findById(backlogItemDTO.getTypeId());
        if (seriesItemType == null) {
            throw new EntityNotFoundException();
        }

        return new BacklogItemImpl(
                backlogItemDTO.getId(),
                backlogItemDTO.getTitle(),
                seriesItemType,
                backlogItemDTO.getNote(),
                user,
                backlogItemDTO.getCreationDate()
        );
    }
}

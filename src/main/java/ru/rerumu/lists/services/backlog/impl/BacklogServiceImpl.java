package ru.rerumu.lists.services.backlog.impl;

import lombok.NonNull;
import ru.rerumu.lists.controller.backlog.view.in.BacklogItemCreateView;
import ru.rerumu.lists.controller.backlog.view.in.BacklogItemUpdateView;
import ru.rerumu.lists.crosscut.exception.ClientException;
import ru.rerumu.lists.domain.backlog.BacklogItem;
import ru.rerumu.lists.domain.backlog.BacklogItemFactory;
import ru.rerumu.lists.domain.series.item.SeriesItemType;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserFactory;
import ru.rerumu.lists.services.backlog.BacklogService;

import java.util.List;

public class BacklogServiceImpl implements BacklogService {

    private final BacklogItemFactory backlogItemFactory;
    private final UserFactory userFactory;

    public BacklogServiceImpl(
            BacklogItemFactory backlogItemFactory,
            UserFactory userFactory
    ) {
        this.backlogItemFactory = backlogItemFactory;
        this.userFactory = userFactory;
    }

    @Override
    public void addItemToBacklog(@NonNull Long userId, @NonNull BacklogItemCreateView backlogItemCreateView) {
        // Get user
        User user = userFactory.findById(userId);

        // Get type and check correctness
        SeriesItemType seriesItemType = SeriesItemType.findById(backlogItemCreateView.getType());
        if (seriesItemType == null) {
            throw new ClientException();
        }

        // Create backlog item
        BacklogItem backlogItem = backlogItemFactory.create(
                backlogItemCreateView.getTitle(),
                seriesItemType,
                backlogItemCreateView.getNote(),
                user
        );

        // Save created backlog item
        backlogItem.save();
    }

    @Override
    public List<BacklogItem> getBacklog(@NonNull Long userId) {
        return List.of();
    }

    @Override
    public void updateBacklogItem(@NonNull Long userId, @NonNull Long backlogItemId, @NonNull BacklogItemUpdateView backlogItemUpdateView) {

    }

    @Override
    public void deleteBacklogItem(@NonNull Long userId, @NonNull Long backlogItemId) {

    }
}

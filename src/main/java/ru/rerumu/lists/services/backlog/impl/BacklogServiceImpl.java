package ru.rerumu.lists.services.backlog.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

@Service("backlogServiceImpl")
public class BacklogServiceImpl implements BacklogService {

    private final BacklogItemFactory backlogItemFactory;
    private final UserFactory userFactory;

    @Autowired
    public BacklogServiceImpl(
            BacklogItemFactory backlogItemFactory,
            UserFactory userFactory
    ) {
        this.backlogItemFactory = backlogItemFactory;
        this.userFactory = userFactory;
    }

    @Override
    public BacklogItem addItemToBacklog(@NonNull Long userId, @NonNull BacklogItemCreateView backlogItemCreateView) {
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
                user,
                backlogItemCreateView.getCreationDate()
        );

        // Save created backlog item
        backlogItem.save();

        return backlogItem;
    }

    @Override
    public List<BacklogItem> getBacklog(@NonNull Long userId) {
        // Get user
        User user = userFactory.findById(userId);

        // Load and return backlog items
        return backlogItemFactory.loadByUser(user);
    }

    @Override
    public BacklogItem updateBacklogItem(@NonNull Long userId, @NonNull Long backlogItemId, @NonNull BacklogItemUpdateView backlogItemUpdateView) {
        // Get user
        User user = userFactory.findById(userId);

        // Get backlog item
        BacklogItem backlogItem = backlogItemFactory.loadById(user, backlogItemId);

        // Update title
        backlogItem.updateTitle(backlogItemUpdateView.getTitle());

        // Get type and check correctness
        SeriesItemType seriesItemType = SeriesItemType.findById(backlogItemUpdateView.getType());
        if (seriesItemType == null) {
            throw new ClientException();
        }

        // Update type
        backlogItem.updateType(seriesItemType);

        // Update note
        backlogItem.updateNote(backlogItemUpdateView.getNote());

        // Update creation date
        backlogItem.updateCreationDate(backlogItemUpdateView.getCreationDate());

        // Save updated backlog item
        backlogItem.save();

        return backlogItem;
    }

    @Override
    public void deleteBacklogItem(@NonNull Long userId, @NonNull Long backlogItemId) {
        // Get user
        User user = userFactory.findById(userId);

        // Get backlog item
        BacklogItem backlogItem = backlogItemFactory.loadById(user, backlogItemId);

        // Delete backlog item
        backlogItem.delete();
    }
}

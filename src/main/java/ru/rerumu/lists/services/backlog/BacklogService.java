package ru.rerumu.lists.services.backlog;

import lombok.NonNull;
import ru.rerumu.lists.controller.backlog.view.in.BacklogItemCreateView;
import ru.rerumu.lists.controller.backlog.view.in.BacklogItemUpdateView;
import ru.rerumu.lists.domain.backlog.BacklogItem;

import java.util.List;

public interface BacklogService {

    /**
     * Add item to backlog
     */
    void addItemToBacklog(@NonNull Long userId, @NonNull BacklogItemCreateView backlogItemCreateView);

    List<BacklogItem> getBacklog(@NonNull Long userId);

    void updateBacklogItem(@NonNull Long userId, @NonNull Long backlogItemId, @NonNull BacklogItemUpdateView backlogItemUpdateView);

    void deleteBacklogItem(@NonNull Long userId, @NonNull Long backlogItemId);

}

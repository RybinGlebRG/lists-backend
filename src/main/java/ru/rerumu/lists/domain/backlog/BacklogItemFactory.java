package ru.rerumu.lists.domain.backlog;

import lombok.NonNull;
import ru.rerumu.lists.domain.series.item.SeriesItemType;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BacklogItemFactory {

    /**
     * Create new backlog item for user
     */
    BacklogItem create(@NonNull String title, @NonNull SeriesItemType type, String note, @NonNull User user, LocalDateTime creationDate);

    /**
     * Load backlog items owned by user
     */
    List<BacklogItem> loadByUser(@NonNull User user);

    /**
     * Load specified backlog item owned by user
     */
    BacklogItem loadById(@NonNull User user, @NonNull Long backlogItemId);

}

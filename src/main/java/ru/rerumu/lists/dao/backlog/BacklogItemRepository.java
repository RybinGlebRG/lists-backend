package ru.rerumu.lists.dao.backlog;

import lombok.NonNull;
import ru.rerumu.lists.domain.backlog.BacklogItem;
import ru.rerumu.lists.domain.seriesitem.SeriesItemType;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BacklogItemRepository {

    void delete(BacklogItem backlogItem);

    void save(BacklogItem backlogItem);

    /**
     * Create new backlog item for user
     */
    BacklogItem create(@NonNull String title, @NonNull SeriesItemType type, String note, @NonNull User user, LocalDateTime creationDate);

    @NonNull
    List<BacklogItem> findByUser(@NonNull User user);

    @NonNull
    BacklogItem findById(@NonNull Long id, @NonNull User user);

    Long getNextId();

}

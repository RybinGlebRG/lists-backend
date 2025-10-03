package ru.rerumu.lists.domain.backlog;

import lombok.NonNull;
import ru.rerumu.lists.domain.series.item.SeriesItemType;
import ru.rerumu.lists.domain.user.User;

public interface BacklogItemFactory {

    BacklogItem create(@NonNull String title, @NonNull SeriesItemType type, String note, @NonNull User user);

}

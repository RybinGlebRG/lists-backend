package ru.rerumu.lists.domain.backlog;

import ru.rerumu.lists.domain.base.Entity;
import ru.rerumu.lists.domain.series.item.SeriesItemType;

import java.time.LocalDateTime;

public interface BacklogItem extends Entity {

    String getTitle();
    SeriesItemType getType();
    String getNote();
    LocalDateTime getCreationDate();

    void updateTitle(String title);
    void updateType(SeriesItemType type);
    void updateNote(String note);
    void updateCreationDate(LocalDateTime creationDate);
}

package ru.rerumu.lists.domain.backlog;

import ru.rerumu.lists.domain.base.Entity;
import ru.rerumu.lists.domain.seriesitem.SeriesItemType;

import java.time.LocalDateTime;

public interface BacklogItem extends Entity {

    String getTitle();
    SeriesItemType getType();
    String getNote();
    LocalDateTime getCreationDate();

    void setTitle(String title);
    void setType(SeriesItemType type);
    void setNote(String note);
    void setCreationDate(LocalDateTime creationDate);
}

package ru.rerumu.lists.domain.backlog;

import ru.rerumu.lists.domain.base.Entity;

import java.time.LocalDateTime;

public interface BacklogItem extends Entity {

    String getTitle();
    Long getType();
    String getNote();
    LocalDateTime getCreationDate();
}

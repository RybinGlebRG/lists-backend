package ru.rerumu.lists.domain.backlog.impl;

import lombok.Getter;
import ru.rerumu.lists.domain.backlog.BacklogItem;
import ru.rerumu.lists.domain.seriesitem.SeriesItemType;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;

public class BacklogItemImpl implements BacklogItem {

    private final Long id;

    @Getter
    private String title;

    @Getter
    private SeriesItemType type;

    @Getter
    private String note;

    private final User user;

    @Getter
    private LocalDateTime creationDate;

    public BacklogItemImpl(
            Long id,
            String title,
            SeriesItemType type,
            String note,
            User user,
            LocalDateTime creationDate
    ) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.note = note;
        this.user = user;
        this.creationDate = creationDate;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setType(SeriesItemType type) {
        this.type = type;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}

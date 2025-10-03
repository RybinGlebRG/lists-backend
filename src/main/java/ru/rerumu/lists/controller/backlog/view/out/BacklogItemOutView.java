package ru.rerumu.lists.controller.backlog.view.out;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BacklogItemOutView {

    private final String title;
    private final Long type;
    private final String note;
    private final LocalDateTime creationDate;

    public BacklogItemOutView(String title, Long type, String note, LocalDateTime creationDate) {
        this.title = title;
        this.type = type;
        this.note = note;
        this.creationDate = creationDate;
    }
}

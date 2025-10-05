package ru.rerumu.lists.controller.backlog.view.in;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BacklogItemCreateView {

    private final String title;
    private final Long type;
    private final String note;
    private final LocalDateTime creationDate;

    @JsonCreator
    public BacklogItemCreateView(String title, Long type, String note, LocalDateTime creationDate) {
        this.title = title;
        this.type = type;
        this.note = note;
        this.creationDate = creationDate;
    }
}

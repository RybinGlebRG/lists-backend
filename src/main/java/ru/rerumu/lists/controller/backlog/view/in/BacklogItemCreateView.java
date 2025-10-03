package ru.rerumu.lists.controller.backlog.view.in;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class BacklogItemCreateView {

    private final String title;
    private final Long type;
    private final String note;

    @JsonCreator
    public BacklogItemCreateView(String title, Long type, String note) {
        this.title = title;
        this.type = type;
        this.note = note;
    }
}

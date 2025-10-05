package ru.rerumu.lists.controller.backlog.view.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BacklogItemOutView {

    private final Long id;
    private final String title;
    private final Long type;
    private final String note;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime creationDate;

    public BacklogItemOutView(Long id, String title, Long type, String note, LocalDateTime creationDate) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.note = note;
        this.creationDate = creationDate;
    }
}

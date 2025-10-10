package ru.rerumu.lists.controller.backlog.view.in;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class BacklogItemEventCreateView {

    private final Long eventTypeId;

    @JsonCreator
    public BacklogItemEventCreateView(Long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }
}

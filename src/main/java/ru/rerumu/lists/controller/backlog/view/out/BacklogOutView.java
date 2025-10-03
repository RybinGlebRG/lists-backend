package ru.rerumu.lists.controller.backlog.view.out;

import lombok.Getter;

import java.util.List;

@Getter
public class BacklogOutView {

    private final List<BacklogItemOutView> items;

    public BacklogOutView(List<BacklogItemOutView> items) {
        this.items = items;
    }
}

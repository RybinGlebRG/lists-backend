package ru.rerumu.lists.controller.book.view.out;

import lombok.Getter;

public class BookStatusView {

    @Getter
    private final Integer statusId;

    @Getter
    private final String statusName;

    public BookStatusView(Integer statusId, String statusName) {
        this.statusId = statusId;
        this.statusName = statusName;
    }
}

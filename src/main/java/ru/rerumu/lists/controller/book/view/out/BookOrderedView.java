package ru.rerumu.lists.controller.book.view.out;

import lombok.Getter;

public class BookOrderedView {

    @Getter
    private final BookView bookView;

    @Getter
    private final Integer order;

    public BookOrderedView(BookView bookView, Integer order) {
        this.bookView = bookView;
        this.order = order;
    }
}

package ru.rerumu.lists.controller.book.view.out;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class BookListView {

    private final List<BookView> items;

    public BookListView(@NonNull List<BookView> items) {
        this.items = new ArrayList<>(items);
    }

    public List<BookView> getItems() {
        return new ArrayList<>(items);
    }
}

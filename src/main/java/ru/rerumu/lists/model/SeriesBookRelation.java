package ru.rerumu.lists.model;

import ru.rerumu.lists.model.book.BookImpl;

public record SeriesBookRelation(BookImpl book, Series series, Long order) {

    public SeriesBookRelation {
        book = book.clone();
        series = series.clone();
    }

    @Override
    public Series series() {
        return series.clone();
    }

    @Override
    public BookImpl book() {
        return book.clone();
    }
}

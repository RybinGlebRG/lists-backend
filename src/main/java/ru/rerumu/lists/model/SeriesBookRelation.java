package ru.rerumu.lists.model;

import ru.rerumu.lists.model.book.Book;
import ru.rerumu.lists.model.series.impl.SeriesImpl;

public record SeriesBookRelation(Book book, SeriesImpl series, Long order) {

    public SeriesBookRelation {
        book = book;
        series = series.clone();
    }

    @Override
    public SeriesImpl series() {
        return series.clone();
    }

    @Override
    public Book book() {
        return book;
    }
}

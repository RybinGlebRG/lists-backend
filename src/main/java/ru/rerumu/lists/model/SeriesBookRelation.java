package ru.rerumu.lists.model;

import ru.rerumu.lists.model.book.Book;
import ru.rerumu.lists.model.series.Series;

public record SeriesBookRelation(Book book, Series series, Long order) {

    public SeriesBookRelation {
        book = book;
        series = series.clone();
    }

    @Override
    public Series series() {
        return series.clone();
    }

    @Override
    public Book book() {
        return book;
    }
}

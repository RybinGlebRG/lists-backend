package ru.rerumu.lists.model;

public record SeriesBookRelation(Book book, Series series, Long order) {

    public SeriesBookRelation {
        book = book.clone();
        series = series.clone();
    }

    @Override
    public Series series() {
        return series.clone();
    }

    @Override
    public Book book() {
        return book.clone();
    }
}

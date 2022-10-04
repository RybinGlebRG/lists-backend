package ru.rerumu.lists.model;

import ru.rerumu.lists.exception.EmptyMandatoryParameterException;

import java.util.Objects;

public final class SeriesBookRelation {

    private final Book book;
    private final Series series;

    private final Long order;

    public SeriesBookRelation(Book book, Series series, Long order)  {
        this.book = book.clone();
        this.series = series.clone();
        this.order = order;
    }

    public Series getSeries() {
        return series.clone();
    }

    public Book getBook() {
        return book.clone();
    }

    public Long getOrder() {
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeriesBookRelation that = (SeriesBookRelation) o;
        return Objects.equals(book, that.book) && Objects.equals(series, that.series) && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, series, order);
    }
}

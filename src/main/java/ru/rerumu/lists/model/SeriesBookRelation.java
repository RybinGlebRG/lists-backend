package ru.rerumu.lists.model;

import ru.rerumu.lists.exception.EmptyMandatoryParameterException;

public final class SeriesBookRelation {

    private final Book book;
    private final Series series;

    public SeriesBookRelation(Book book, Series series) throws EmptyMandatoryParameterException {
        this.book = new Book.Builder(book).build();
        this.series = series;
    }

    // TODO: Make immutable
    public Series getSeries() {
        return series;
    }

    public Book getBook() throws EmptyMandatoryParameterException {
        return new Book.Builder(book).build();
    }
}

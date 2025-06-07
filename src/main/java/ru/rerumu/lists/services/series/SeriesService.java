package ru.rerumu.lists.services.series;

import ru.rerumu.lists.model.book.Book;
import ru.rerumu.lists.model.book.impl.BookImpl;
import ru.rerumu.lists.model.series.Series;
import ru.rerumu.lists.model.series.impl.SeriesImpl;
import ru.rerumu.lists.views.BookSeriesAddView;
import ru.rerumu.lists.controller.series.view.in.SeriesUpdateView;

import java.util.List;

public interface SeriesService {

    List<Series> getAll(Long userId);

    Series getSeries(Long seriesId, Long userId);

    void add(Long userId, BookSeriesAddView bookSeriesAddView);

    void delete(Long seriesId, Long userId);

    void updateSeries(Long seriesId, Long userId, SeriesUpdateView seriesUpdateView);

    List<Series> findByBook(Book book, Long userId);
}

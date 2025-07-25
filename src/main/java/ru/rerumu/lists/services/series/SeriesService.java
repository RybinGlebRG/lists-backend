package ru.rerumu.lists.services.series;

import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.views.BookSeriesAddView;
import ru.rerumu.lists.controller.series.view.in.SeriesUpdateView;

import java.util.List;

public interface SeriesService {

    List<Series> findAll(Long userId);

    Series findById(Long seriesId, Long userId);

    Series add(Long userId, BookSeriesAddView bookSeriesAddView);

    void delete(Long seriesId, Long userId);

    void updateSeries(Long seriesId, Long userId, SeriesUpdateView seriesUpdateView);

    List<Series> findByBook(Book book, Long userId);
}

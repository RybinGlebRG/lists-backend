package ru.rerumu.lists.domain.book;

import ru.rerumu.lists.domain.series.Series;

public record OrderedSeries(
        Series series,
        Long seriesOrder
) {
}

package ru.rerumu.lists.domain.series;

public record OrderedSeries(
        Series series,
        Long seriesOrder
) {
}

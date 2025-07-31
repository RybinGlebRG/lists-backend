package ru.rerumu.lists.domain;

public record SeriesBookRelation(
        Long bookId,
        Long seriesId,
        Long order,
        Long userId
) {}

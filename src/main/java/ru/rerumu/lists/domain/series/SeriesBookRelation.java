package ru.rerumu.lists.domain.series;

public record SeriesBookRelation(
        Long bookId,
        Long seriesId,
        Long userId
) implements SeriesItemRelation {

    @Override
    public Long getBookId() {
        return bookId;
    }
}

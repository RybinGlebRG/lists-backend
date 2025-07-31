package ru.rerumu.lists.domain.series;

public record SeriesBookRelationDto (
        Long seriesId,
        Long bookId,
        Long userId,
        Long seriesOrder
) implements SeriesItemRelationDTO {

    @Override
    public Long getOrder() {
        return seriesOrder;
    }
}

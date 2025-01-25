package ru.rerumu.lists.model.dto;

import ru.rerumu.lists.model.series.item.SeriesItem;
import ru.rerumu.lists.model.series.item.SeriesItemDTO;

public record NullDTO() implements SeriesItemDTO {
    @Override
    public SeriesItem toDomain() {
        return null;
    }
}

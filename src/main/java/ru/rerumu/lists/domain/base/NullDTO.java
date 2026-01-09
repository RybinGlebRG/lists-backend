package ru.rerumu.lists.domain.base;

import ru.rerumu.lists.domain.series.item.SeriesItem;
import ru.rerumu.lists.dao.series.item.SeriesItemDTO;

public record NullDTO() implements SeriesItemDTO {
    @Override
    public SeriesItem toDomain() {
        return null;
    }
}

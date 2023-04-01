package ru.rerumu.lists.model.dto;

import ru.rerumu.lists.model.SeriesItem;

public record NullDTO() implements SeriesItemDTO {
    @Override
    public SeriesItem toDomain() {
        return null;
    }
}

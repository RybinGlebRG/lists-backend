package ru.rerumu.lists.views;

import java.time.LocalDateTime;

public record ReadingRecordAddView(
        Integer statusId,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}

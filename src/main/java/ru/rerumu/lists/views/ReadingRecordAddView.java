package ru.rerumu.lists.views;

import java.time.LocalDateTime;

public record ReadingRecordAddView(
        Long statusId,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}

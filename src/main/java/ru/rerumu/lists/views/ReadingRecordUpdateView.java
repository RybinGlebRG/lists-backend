package ru.rerumu.lists.views;

import java.time.LocalDateTime;

public record ReadingRecordUpdateView(
        Long statusId,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}

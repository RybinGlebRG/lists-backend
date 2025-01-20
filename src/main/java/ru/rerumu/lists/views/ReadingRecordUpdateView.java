package ru.rerumu.lists.views;

import java.time.LocalDateTime;

public record ReadingRecordUpdateView(
        Integer statusId,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}

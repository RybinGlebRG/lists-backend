package ru.rerumu.lists.views;

import java.time.LocalDateTime;

public record GameAddView(
        String title,
        LocalDateTime createDateUTC,
        String note
) {
}

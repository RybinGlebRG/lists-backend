package ru.rerumu.lists.controller.games.views;

import java.time.LocalDateTime;

public record GameAddView(
        String title,
        LocalDateTime createDateUTC,
        String note
) {
}

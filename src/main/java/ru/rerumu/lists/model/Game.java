package ru.rerumu.lists.model;

import java.time.LocalDateTime;

public record Game(String title, User user, LocalDateTime createDateUTC) implements SeriesItem {
}

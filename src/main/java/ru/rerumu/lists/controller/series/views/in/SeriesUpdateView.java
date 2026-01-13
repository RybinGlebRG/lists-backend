package ru.rerumu.lists.controller.series.views.in;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SeriesUpdateView(
        @JsonProperty("title") String title
) {
}

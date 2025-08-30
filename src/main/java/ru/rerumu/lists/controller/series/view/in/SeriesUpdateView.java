package ru.rerumu.lists.controller.series.view.in;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SeriesUpdateView(
        @JsonProperty("title") String title,
        @JsonProperty("items") List<SeriesUpdateItem> itemList
) {
}

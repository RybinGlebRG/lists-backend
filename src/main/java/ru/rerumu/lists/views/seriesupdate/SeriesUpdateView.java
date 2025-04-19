package ru.rerumu.lists.views.seriesupdate;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SeriesUpdateView(
        @JsonProperty("title") String title,
        @JsonProperty("items") List<SeriesUpdateItem> itemList
) {
}

package ru.rerumu.lists.views.series_update;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SeriesUpdateItem(
        @JsonProperty("itemType")String itemType,
        @JsonProperty("itemId")Long itemId,
        @JsonProperty("itemOrder")Long itemOrder
) {}

package ru.rerumu.lists.controller.series.view.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.rerumu.lists.model.series.item.SeriesItemType;

public record SeriesUpdateItem(
        @JsonProperty("itemType") SeriesItemType itemType,
        @JsonProperty("itemId")Long itemId,
        @JsonProperty("itemOrder")Long itemOrder
) {}

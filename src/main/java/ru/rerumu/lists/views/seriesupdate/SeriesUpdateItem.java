package ru.rerumu.lists.views.seriesupdate;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.rerumu.lists.model.series.item.SeriesItemType;

public record SeriesUpdateItem(
        @JsonProperty("itemType") SeriesItemType itemType,
        @JsonProperty("itemId")Long itemId,
        @JsonProperty("itemOrder")Long itemOrder
) {}

package ru.rerumu.lists.controller.series.views.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.rerumu.lists.domain.seriesitem.SeriesItemType;

public record SeriesUpdateItem(
        @JsonProperty("itemType") SeriesItemType itemType,
        @JsonProperty("itemId")Long itemId,
        @JsonProperty("itemOrder")Long itemOrder
) {}

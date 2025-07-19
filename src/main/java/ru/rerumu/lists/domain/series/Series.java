package ru.rerumu.lists.domain.series;

import ru.rerumu.lists.domain.base.Entity;
import ru.rerumu.lists.domain.series.item.SeriesItem;

import java.util.List;

public interface Series extends Entity {

    String getTitle();

    SeriesDTOv2 toDTO();

    List<SeriesItem> getItemsList();

    Long getItemsCountAsLong();
}

package ru.rerumu.lists.model.series;

import ru.rerumu.lists.model.series.item.SeriesItemDTOv2;

import java.util.ArrayList;
import java.util.List;

public record SeriesDTOv2(
        Long seriesId,
        Long userId,
        String title,
        Integer bookCount,
        List<SeriesItemDTOv2> items
) {

    public SeriesDTOv2 {
        items = new ArrayList<>(items);
    }
}

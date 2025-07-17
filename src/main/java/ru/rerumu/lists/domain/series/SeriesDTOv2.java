package ru.rerumu.lists.domain.series;

import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.domain.base.EntityDTO;
import ru.rerumu.lists.domain.series.impl.SeriesImpl;
import ru.rerumu.lists.domain.series.item.SeriesItemDTOv2;

import java.util.ArrayList;
import java.util.List;

public record SeriesDTOv2(
        Long seriesId,
        Long userId,
        String title,
        List<SeriesItemDTOv2> items
) implements EntityDTO<SeriesImpl> {

    public SeriesDTOv2 {
        items = new ArrayList<>(items);
    }

    @Override
    public SeriesImpl toDomain() {
        throw new NotImplementedException();
    }
}

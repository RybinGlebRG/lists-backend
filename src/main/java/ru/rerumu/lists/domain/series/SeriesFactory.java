package ru.rerumu.lists.domain.series;

import lombok.NonNull;
import ru.rerumu.lists.domain.series.impl.SeriesImpl;
import ru.rerumu.lists.domain.series.item.SeriesItem;
import ru.rerumu.lists.domain.user.User;

import java.util.List;

public interface SeriesFactory {

    List<Series> findAll(@NonNull User user);

    Series findById(@NonNull User user, @NonNull Long seriesId);

    Series createSeries(
            @NonNull String title,
            @NonNull List<SeriesItem> itemsList,
            @NonNull User user
    );

    SeriesImpl fromDTOv2(SeriesDTOv2 seriesDTO);

    List<Series> findByBook(@NonNull Long bookId, @NonNull Long userId);
}

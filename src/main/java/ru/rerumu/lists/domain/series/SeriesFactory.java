package ru.rerumu.lists.domain.series;

import lombok.NonNull;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.series.impl.SeriesImpl;
import ru.rerumu.lists.domain.user.User;

import java.util.List;

public interface SeriesFactory {

    List<Series> findAll(@NonNull User user);

    Series findById(@NonNull User user, @NonNull Long seriesId);

    Series buildSeries(
            @NonNull Long id,
            @NonNull String title,
            @NonNull User user
    );

    @NonNull
    Series buildSeries(
            @NonNull Long id,
            @NonNull String title,
            @NonNull User user,
            @NonNull EntityState entityState
            );

    SeriesImpl fromDTOv2(SeriesDTOv2 seriesDTO);

    List<Series> findByBook(@NonNull Long bookId, @NonNull Long userId);
}

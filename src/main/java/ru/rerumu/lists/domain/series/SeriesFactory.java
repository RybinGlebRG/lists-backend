package ru.rerumu.lists.domain.series;

import lombok.NonNull;
import ru.rerumu.lists.domain.user.User;

import java.util.List;

public interface SeriesFactory {

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
            @NonNull List<SeriesItemRelation> seriesItemRelation
    );
}

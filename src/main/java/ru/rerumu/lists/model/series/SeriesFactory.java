package ru.rerumu.lists.model.series;

import lombok.NonNull;
import ru.rerumu.lists.model.series.item.SeriesItem;
import ru.rerumu.lists.model.user.User;

import java.util.List;

public interface SeriesFactory {

    List<Series> findAll(@NonNull User user);

    Series findById(@NonNull User user, @NonNull Long seriesId);

    Series createSeries(
            @NonNull String title,
            @NonNull List<SeriesItem> itemsList,
            @NonNull User user
    );
}

package ru.rerumu.lists.dao.series;

import lombok.NonNull;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.user.User;

import java.util.List;

public interface SeriesRepository {

    void delete(long seriesId);

    @Deprecated
    List<Series> findByBook(@NonNull Long bookId, @NonNull Long userId);

    List<Series> findByIds(@NonNull List<Long> seriesIds, @NonNull Long userId);

    void save(@NonNull Series series);

    Long getNextId();

    @NonNull
    List<Series> findByUser(User user);

    Series findById(@NonNull Long seriesId, @NonNull User user);

    /**
     * Create series
     */
    @NonNull
    Series create(
            @NonNull String title,
            @NonNull User user
    );
}

package ru.rerumu.lists.domain.series;

import lombok.NonNull;
import ru.rerumu.lists.dao.series.SeriesMyBatisEntity;
import ru.rerumu.lists.domain.base.EntityState;
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
            @NonNull EntityState entityState,
            @NonNull List<SeriesItemRelation> seriesItemRelation
    );

    Series fromMyBatisEntity(SeriesMyBatisEntity myBatisEntity,  User user);

//    List<Series> findByBook(@NonNull Long bookId, @NonNull Long userId);
}

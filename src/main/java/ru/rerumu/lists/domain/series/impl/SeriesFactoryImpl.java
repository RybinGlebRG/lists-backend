package ru.rerumu.lists.domain.series.impl;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.domain.series.SeriesItemRelation;
import ru.rerumu.lists.domain.user.User;

import java.util.ArrayList;
import java.util.List;


@Component
public class SeriesFactoryImpl implements SeriesFactory {

    @Override
    @NonNull
    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    public Series buildSeries(
            @NonNull Long id,
            @NonNull String title,
            @NonNull User user
    ) {
        SeriesImpl series = new SeriesImpl(
                id,
                title,
                new ArrayList<>(),
                user,
                new ArrayList<>()
        );
        return series;
    }

    @Override
    @NonNull
    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    public Series buildSeries(
            @NonNull Long id,
            @NonNull String title,
            @NonNull User user,
            @NonNull List<SeriesItemRelation> seriesItemRelation
            ) {
        SeriesImpl series = new SeriesImpl(
                id,
                title,
                new ArrayList<>(),
                user,
                seriesItemRelation
        );
        return series;
    }
}

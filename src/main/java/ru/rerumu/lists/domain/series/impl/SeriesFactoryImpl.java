package ru.rerumu.lists.domain.series.impl;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.series.SeriesItemRelationDTO;
import ru.rerumu.lists.dao.series.SeriesMyBatisEntity;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.domain.series.SeriesItemRelation;
import ru.rerumu.lists.domain.series.SeriesItemRelationFactory;
import ru.rerumu.lists.domain.user.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class SeriesFactoryImpl implements SeriesFactory {

    private final SeriesItemRelationFactory seriesItemRelationFactory;

    @Autowired
    public SeriesFactoryImpl(
            @NonNull SeriesItemRelationFactory seriesItemRelationFactory
    ) {
        this.seriesItemRelationFactory = seriesItemRelationFactory;
    }

    @Override
    public Series fromMyBatisEntity(SeriesMyBatisEntity seriesMyBatisEntity, User user) {

        List<SeriesItemRelationDTO> seriesItemRelationDTOList = seriesMyBatisEntity.getSeriesBookRelationDtoList().stream()
                .map(item -> (SeriesItemRelationDTO)item)
                .sorted(Comparator.comparingLong(SeriesItemRelationDTO::getOrder))
                .collect(Collectors.toCollection(ArrayList::new));

        List<SeriesItemRelation> seriesItemRelations = seriesItemRelationFactory.fromDTO(seriesItemRelationDTOList);

        return buildSeries(
                seriesMyBatisEntity.getSeriesId(),
                seriesMyBatisEntity.getTitle(),
                user,
                seriesItemRelations
        );
    }

    @Override
    @NonNull
    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
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
    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
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

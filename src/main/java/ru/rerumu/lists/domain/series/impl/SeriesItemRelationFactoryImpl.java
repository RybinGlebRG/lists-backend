package ru.rerumu.lists.domain.series.impl;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.domain.series.SeriesBookRelation;
import ru.rerumu.lists.domain.series.SeriesBookRelationDto;
import ru.rerumu.lists.domain.series.SeriesItemRelation;
import ru.rerumu.lists.domain.series.SeriesItemRelationDTO;
import ru.rerumu.lists.domain.series.SeriesItemRelationFactory;

import java.util.ArrayList;
import java.util.List;

@Component
public class SeriesItemRelationFactoryImpl implements SeriesItemRelationFactory {

    @Override
    @NonNull
    public List<SeriesItemRelation> fromDTO(@NonNull List<SeriesItemRelationDTO> seriesItemRelationDTOList) {
        List<SeriesItemRelation> seriesItemRelations = new ArrayList<>();

        for (SeriesItemRelationDTO item: seriesItemRelationDTOList) {
            if (item instanceof SeriesBookRelationDto seriesBookRelationDto) {
                seriesItemRelations.add(
                        new SeriesBookRelation(
                                seriesBookRelationDto.bookId(),
                                seriesBookRelationDto.seriesId(),
                                seriesBookRelationDto.userId()
                        )
                );
            } else {
                throw new ServerException();
            }
        }

        return seriesItemRelations;
    }

}

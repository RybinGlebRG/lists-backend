package ru.rerumu.lists.dao.series;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.dao.base.MyBatisEntity;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesBookRelation;
import ru.rerumu.lists.dao.series.impl.SeriesBookRelationDto;
import ru.rerumu.lists.domain.series.SeriesItemRelation;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class SeriesMyBatisEntity implements MyBatisEntity  {

    private Long seriesId;
    private Long userId;
    private String title;
    private List<SeriesBookRelationDto> seriesBookRelationDtoList = new ArrayList<>();

    public static SeriesMyBatisEntity fromDomain(Series series) {

        List<SeriesBookRelationDto> seriesBookRelationDtoList = new ArrayList<>();
        for (SeriesItemRelation seriesItemRelation: series.getSeriesItemRelations()) {
            if (seriesItemRelation instanceof SeriesBookRelation seriesBookRelation) {
                seriesBookRelationDtoList.add(
                        new SeriesBookRelationDto(
                                seriesBookRelation.seriesId(),
                                seriesBookRelation.bookId(),
                                seriesBookRelation.userId(),
                                (long) series.getSeriesItemRelations().indexOf(seriesBookRelation)
                        )
                );
            } else {
                throw new ServerException();
            }
        }

        return new SeriesMyBatisEntity(
                series.getId(),
                series.getUser().userId(),
                series.getTitle(),
                seriesBookRelationDtoList
        );
    }

}

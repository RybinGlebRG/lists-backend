package ru.rerumu.lists.domain.series;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.dao.base.MyBatisEntity;
import ru.rerumu.lists.domain.base.EntityDTO;
import ru.rerumu.lists.domain.series.impl.SeriesImpl;
import ru.rerumu.lists.domain.series.item.SeriesItemDTOv2;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SeriesDTOv2 implements EntityDTO<SeriesImpl>, MyBatisEntity {

    private Long seriesId;
    private Long userId;
    private String title;
    private List<SeriesItemDTOv2> items = new ArrayList<>();
    private List<SeriesBookRelationDto> seriesBookRelationDtoList = new ArrayList<>();

    @Override
    public SeriesImpl toDomain() {
        throw new NotImplementedException();
    }
}

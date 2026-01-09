package ru.rerumu.lists.domain.series;

import lombok.NonNull;
import ru.rerumu.lists.dao.series.SeriesItemRelationDTO;

import java.util.List;

public interface SeriesItemRelationFactory {

    @NonNull
    List<SeriesItemRelation> fromDTO(@NonNull List<SeriesItemRelationDTO> seriesItemRelationDTOList);

}

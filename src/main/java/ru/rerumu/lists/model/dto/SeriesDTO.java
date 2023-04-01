package ru.rerumu.lists.model.dto;

import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.SeriesItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SeriesDTO implements EntityDTO<Series> {

    public  Long seriesId;
    public  Long seriesListId;
    public  String title;

    // TODO: remove
    public Integer bookCount=0;
    public List<SeriesItemOrderDTO> seriesItemOrderDTOList;

    public SeriesDTO(){}

    public Series toSeries(){
        List<SeriesItem> tmp = seriesItemOrderDTOList.stream()
                .sorted(Comparator.comparing(SeriesItemOrderDTO::getOrder))
                .map(SeriesItemOrderDTO::getItemDTO)
                .map(SeriesItemDTO::toDomain)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));

        return new Series.Builder()
                .seriesId(seriesId)
                .title(title)
                .readListId(seriesListId)
                .bookCount(bookCount)
                .itemList(tmp)
                .build();
    }

    @Override
    public Series toDomain() {
        return toSeries();
    }

}

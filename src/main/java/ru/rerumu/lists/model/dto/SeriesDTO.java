package ru.rerumu.lists.model.dto;

import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.EntityDTO;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.SeriesItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SeriesDTO implements EntityDTO<Series> {

    public  Long seriesId;
    public  Long seriesListId;
    public  String title;
    public Integer bookCount;
    public List<SeriesItemOrderDTO> seriesItemOrderDTOList;

    public SeriesDTO(){}

    public Series toSeries(){
        List<SeriesItem> tmp = seriesItemOrderDTOList.stream()
                .sorted(Comparator.comparing(item ->item.order))
                .map(item -> {
                    try {
                        return item.itemDTO.toDomain();
                    } catch (EmptyMandatoryParameterException e) {
                        throw new AssertionError(e);
                    }
                })
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

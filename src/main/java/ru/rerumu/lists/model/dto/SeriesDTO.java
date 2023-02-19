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
    public  List<SeriesBookDTO> seriesBookDTOList;
    public List<SeriesTitleDTO> seriesTitleDTOList;

    public SeriesDTO(){}

    public Series toSeries(){
        List<Object> allItems = new ArrayList<>();
        if (seriesBookDTOList != null) {
            seriesBookDTOList.stream()
                    .filter(item -> item.bookDTO != null && item.bookDTO.bookId != null)
                    .forEach(allItems::add);
        }
        if (seriesTitleDTOList != null) {
            seriesTitleDTOList.stream()
                    .filter(item -> item.titleDTO != null && item.titleDTO.titleId != null)
                    .forEach(allItems::add);
        }
        List<SeriesItem> tmp = allItems.stream()
                .sorted(Comparator.comparing(item->{
                    if (item instanceof SeriesBookDTO seriesBookDTO){
                        return seriesBookDTO.order;
                    } else if (item instanceof SeriesTitleDTO seriesTitleDTO){
                        return seriesTitleDTO.order;
                    } else {
                        throw new IllegalArgumentException();
                    }
                }))
                .map(item->{
                    if (item instanceof SeriesBookDTO seriesBookDTO){
                        try {
                            return seriesBookDTO.bookDTO.toBook();
                        } catch (EmptyMandatoryParameterException e) {
                            throw new AssertionError(e);
                        }
                    } else if (item instanceof SeriesTitleDTO seriesTitleDTO){
                        return seriesTitleDTO.titleDTO.toDomain();
                    } else {
                        throw new IllegalArgumentException();
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

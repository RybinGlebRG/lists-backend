package ru.rerumu.lists.model.dto;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.EntityDTO;
import ru.rerumu.lists.model.Series;

import java.time.LocalDateTime;
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

    public SeriesDTO(){}

    public Series toSeries(){
        List<Object> allItems = new ArrayList<>();
        seriesBookDTOList.stream()
                .filter(item->item.order != null)
                .forEach(allItems::add);
        List<Object> tmp = allItems.stream()
                .sorted(Comparator.comparing(item->{
                    if (item instanceof SeriesBookDTO seriesBookDTO){
                        return seriesBookDTO.order;
                    } else {
                        throw new IllegalArgumentException();
                    }
                }))
                .map(item->{
                    if (item instanceof SeriesBookDTO seriesBookDTO){
                        try {
                            return (Object)(seriesBookDTO.bookDTO.toBook());
                        } catch (EmptyMandatoryParameterException e) {
                            throw new AssertionError(e);
                        }
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

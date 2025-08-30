package ru.rerumu.lists.domain.series;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.domain.series.impl.SeriesImpl;
import ru.rerumu.lists.domain.series.item.SeriesItem;
import ru.rerumu.lists.domain.base.EntityDTO;
import ru.rerumu.lists.domain.series.item.SeriesItemDTO;
import ru.rerumu.lists.domain.dto.SeriesItemOrderDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SeriesDTO implements EntityDTO<SeriesImpl> {

    public  Long seriesId;
    public  Long seriesListId;
    public  String title;

    // TODO: remove
    public Integer bookCount=0;
    public List<SeriesItemOrderDTO> seriesItemOrderDTOList;

    public SeriesImpl toSeries(){
        List<SeriesItem> tmp = seriesItemOrderDTOList.stream()
                .sorted(Comparator.comparing(SeriesItemOrderDTO::getOrder))
                .map(SeriesItemOrderDTO::getItemDTO)
                .map(SeriesItemDTO::toDomain)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));

        throw new NotImplementedException();
//        return new SeriesImpl.Builder()
//                .seriesId(seriesId)
//                .title(title)
//                .readListId(seriesListId)
//                .bookCount(bookCount)
//                .itemList(tmp)
//                .build();
    }

    @Override
    public SeriesImpl toDomain() {
        return toSeries();
    }

}

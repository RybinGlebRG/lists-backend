package ru.rerumu.lists.model.series;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.model.series.item.SeriesItem;
import ru.rerumu.lists.model.book.impl.BookFactoryImpl;
import ru.rerumu.lists.model.book.BookDTO;
import ru.rerumu.lists.model.dto.SeriesItemOrderDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SeriesFactory {

    private final BookFactoryImpl bookFactory;

    public SeriesFactory(BookFactoryImpl bookFactory) {
        this.bookFactory = bookFactory;
    }

    public Series fromDTO(SeriesDTO seriesDTO){
        List<SeriesItem> tmp = seriesDTO.seriesItemOrderDTOList.stream()
                .sorted(Comparator.comparing(SeriesItemOrderDTO::getOrder))
                .map(SeriesItemOrderDTO::getItemDTO)
                .map(seriesItemDTO -> {
                    if (seriesItemDTO instanceof BookDTO){
                        return bookFactory.fromDTO((BookDTO) seriesItemDTO);
                    } else {
                        return seriesItemDTO.toDomain();
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));

        return new Series.Builder()
                .seriesId(seriesDTO.seriesId)
                .title(seriesDTO.title)
                .readListId(seriesDTO.seriesListId)
                .bookCount(seriesDTO.bookCount)
                .itemList(tmp)
                .build();
    }
}

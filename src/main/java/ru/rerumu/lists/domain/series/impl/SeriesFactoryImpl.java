package ru.rerumu.lists.domain.series.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.dao.book.BookDtoDao;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.domain.book.BookDTO;
import ru.rerumu.lists.domain.book.BookFactory;
import ru.rerumu.lists.domain.dto.SeriesItemOrderDTO;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesDTO;
import ru.rerumu.lists.domain.series.SeriesDTOv2;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.domain.series.item.SeriesItem;
import ru.rerumu.lists.domain.user.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// TODO
@Component
public class SeriesFactoryImpl implements SeriesFactory {

    private final BookFactory bookFactory;
    private final SeriesRepository seriesRepository;

    @Autowired
    public SeriesFactoryImpl(
            BookFactory bookFactory,
            SeriesRepository seriesRepository
    ) {
        this.bookFactory = bookFactory;
        this.seriesRepository = seriesRepository;
    }

    @Deprecated
    public SeriesImpl fromDTO(SeriesDTO seriesDTO){
        List<SeriesItem> tmp = seriesDTO.seriesItemOrderDTOList.stream()
                .sorted(Comparator.comparing(SeriesItemOrderDTO::getOrder))
                .map(SeriesItemOrderDTO::getItemDTO)
                .map(seriesItemDTO -> {
                    if (seriesItemDTO instanceof BookDTO){
                        return bookFactory.fromDTO((BookDTO) seriesItemDTO);
                    } else if (seriesItemDTO instanceof BookDtoDao bookDtoDao) {
                        return bookFactory.fromDTO(bookDtoDao);
                    } else {
                        return seriesItemDTO.toDomain();
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));


        throw new NotImplementedException();
        // TODO: fix null
//        return new SeriesImpl(
//                seriesDTO.seriesId,
//                seriesDTO.title,
//                tmp,
//                null
//        );
    }

    public SeriesImpl fromDTOv2(SeriesDTOv2 seriesDTO){
        throw new NotImplementedException();
    }

    @Override
    public List<Series> findByBook(@NonNull Long bookId, @NonNull Long userId) {
        throw new NotImplementedException();
    }

    @Override
    public List<Series> findAll(@NonNull User user) {
        throw new NotImplementedException();
    }

    @Override
    public Series findById(@NonNull User user, @NonNull Long seriesId) {
        throw new NotImplementedException();
    }

    @Override
    public Series createSeries(
            @NonNull String title,
            @NonNull List<SeriesItem> itemsList,
            @NonNull User user
    ) {
        throw new NotImplementedException();
//        SeriesImpl series = new SeriesImpl(
//                null,
//                title,
//                itemsList,
//                user
//        );
//
//        seriesRepository.create(series.toDTO());
    }
}

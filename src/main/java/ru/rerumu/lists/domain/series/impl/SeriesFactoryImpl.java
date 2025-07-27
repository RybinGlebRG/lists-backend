package ru.rerumu.lists.domain.series.impl;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesDTO;
import ru.rerumu.lists.domain.series.SeriesDTOv2;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TODO
@Component
public class SeriesFactoryImpl implements SeriesFactory {

    private final SeriesRepository seriesRepository;

    @Autowired
    public SeriesFactoryImpl(
            SeriesRepository seriesRepository
    ) {
        this.seriesRepository = seriesRepository;
    }

    @Deprecated
    public SeriesImpl fromDTO(SeriesDTO seriesDTO){
        throw new NotImplementedException();

//        List<SeriesItem> tmp = seriesDTO.seriesItemOrderDTOList.stream()
//                .sorted(Comparator.comparing(SeriesItemOrderDTO::getOrder))
//                .map(SeriesItemOrderDTO::getItemDTO)
//                .map(seriesItemDTO -> {
//                    if (seriesItemDTO instanceof BookDTO){
//                        return bookFactory.fromDTO((BookDTO) seriesItemDTO);
//                    } else if (seriesItemDTO instanceof BookDtoDao bookDtoDao) {
//                        return bookFactory.fromDTO(bookDtoDao);
//                    } else {
//                        return seriesItemDTO.toDomain();
//                    }
//                })
//                .filter(Objects::nonNull)
//                .collect(Collectors.toCollection(ArrayList::new));



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
        List<SeriesDTOv2> seriesDTOList = seriesRepository.findByBook(bookId, userId);

        return seriesDTOList.stream()
                .map(this::fromDTOv2)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Series> findAll(@NonNull User user) {
        throw new NotImplementedException();
    }

    @Override
    public Series findById(@NonNull User user, @NonNull Long seriesId) {
       SeriesDTOv2 seriesDTOv2 = seriesRepository.findById(seriesId, user).orElse(null);

       if (seriesDTOv2 != null) {
           return fromDTOv2(seriesDTOv2);
       } else {
           throw new EntityNotFoundException();
       }
    }

    @Override
    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
    public Series createSeries(
            @NonNull Long id,
            @NonNull String title,
            @NonNull User user
    ) {
        SeriesImpl series = new SeriesImpl(
                id,
                title,
                new ArrayList<>(),
                user,
                new ArrayList<>(),
                seriesRepository,
                EntityState.NEW
        );
        return series;
    }
}

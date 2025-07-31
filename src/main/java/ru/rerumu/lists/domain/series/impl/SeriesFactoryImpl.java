package ru.rerumu.lists.domain.series.impl;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.dao.series.SeriesBooksRespository;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesDTO;
import ru.rerumu.lists.domain.series.SeriesDTOv2;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.domain.series.SeriesItemRelation;
import ru.rerumu.lists.domain.series.SeriesItemRelationDTO;
import ru.rerumu.lists.domain.series.SeriesItemRelationFactory;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// TODO
@Component
public class SeriesFactoryImpl implements SeriesFactory {

    private final SeriesRepository seriesRepository;
    private final SeriesBooksRespository seriesBooksRespository;
    private final SeriesItemRelationFactory seriesItemRelationFactory;
    private final UserFactory userFactory;

    @Autowired
    public SeriesFactoryImpl(
            @NonNull SeriesRepository seriesRepository,
            @NonNull SeriesBooksRespository seriesBooksRespository,
            @NonNull SeriesItemRelationFactory seriesItemRelationFactory,
            @NonNull UserFactory userFactory
    ) {
        this.seriesRepository = seriesRepository;
        this.seriesBooksRespository = seriesBooksRespository;
        this.seriesItemRelationFactory = seriesItemRelationFactory;
        this.userFactory = userFactory;
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

    @Deprecated
    public SeriesImpl fromDTOv2(SeriesDTOv2 seriesDTO){
        throw new NotImplementedException();
    }

    @Override
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    public List<Series> findByBook(@NonNull Long bookId, @NonNull Long userId) {
        List<SeriesDTOv2> seriesDTOList = seriesRepository.findByBook(bookId, userId);

        return seriesDTOList.stream()
                .map(item -> buildSeries(
                        item.getSeriesId(),
                        item.getTitle(),
                        userFactory.findById(item.getUserId()),
                        EntityState.PERSISTED,
                        seriesItemRelationFactory.fromDTO(
                                new ArrayList<>(item.getSeriesBookRelationDtoList())
                        )
                ))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Series> findAll(@NonNull User user) {
        throw new NotImplementedException();
    }

    @Override
    @Loggable(value = Loggable.DEBUG, prepend = true, trim = false)
    public Series findById(@NonNull User user, @NonNull Long seriesId) {
       SeriesDTOv2 seriesDTOv2 = seriesRepository.findById(seriesId, user).orElse(null);

       if (seriesDTOv2 != null) {

           List<SeriesItemRelationDTO> seriesItemRelationDTOList = seriesDTOv2.getSeriesBookRelationDtoList().stream()
                   .map(item -> (SeriesItemRelationDTO)item)
                   .sorted(Comparator.comparingLong(SeriesItemRelationDTO::getOrder))
                   .collect(Collectors.toCollection(ArrayList::new));

           return buildSeries(
                   seriesDTOv2.getSeriesId(),
                   seriesDTOv2.getTitle(),
                   user,
                   EntityState.PERSISTED,
                   seriesItemRelationFactory.fromDTO(seriesItemRelationDTOList)
           );
       } else {
           throw new EntityNotFoundException();
       }
    }

    @Override
    @NonNull
    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
    public Series buildSeries(
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
                EntityState.NEW,
                seriesBooksRespository,
                new ArrayList<>()
        );
        return series;
    }

    @Override
    @NonNull
    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
    public Series buildSeries(
            @NonNull Long id,
            @NonNull String title,
            @NonNull User user,
            @NonNull EntityState entityState,
            @NonNull List<SeriesItemRelation> seriesItemRelation
            ) {
        SeriesImpl series = new SeriesImpl(
                id,
                title,
                new ArrayList<>(),
                user,
                new ArrayList<>(),
                seriesRepository,
                entityState,
                seriesBooksRespository,
                seriesItemRelation
        );
        return series;
    }
}

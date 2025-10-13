package ru.rerumu.lists.domain.series.impl;

import com.jcabi.aspects.Loggable;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.rerumu.lists.crosscut.DeepCopyable;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.crosscut.exception.UnsupportedMethodException;
import ru.rerumu.lists.dao.series.SeriesBooksRespository;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.domain.base.EntityBaseImpl;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.dto.SeriesBookRelationDTO;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesBookRelation;
import ru.rerumu.lists.domain.series.SeriesBookRelationDto;
import ru.rerumu.lists.domain.series.SeriesDTOv2;
import ru.rerumu.lists.domain.series.SeriesItemRelation;
import ru.rerumu.lists.domain.series.item.SeriesItem;
import ru.rerumu.lists.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ToString(callSuper = true, doNotUseGetters = true)
@Slf4j
public class SeriesImpl extends EntityBaseImpl<SeriesImpl> implements Series, DeepCopyable<SeriesImpl> {

    private final Long seriesId;

    @Getter
    private final String title;

    @Getter
    private final List<SeriesItem> itemsList;

    @Getter
    private final User user;

    @Deprecated
    private final List<SeriesBookRelationDTO> seriesBookRelationDTOList;

    private final SeriesRepository seriesRepository;

    private final SeriesBooksRespository seriesBooksRespository;

    private final List<SeriesItemRelation> seriesItemRelations;


    public SeriesImpl(
            Long seriesId,
            @NonNull String title,
            @NonNull List<SeriesItem> itemsList,
            @NonNull User user,
            @NonNull List<SeriesBookRelationDTO> seriesBookRelationDTOList,
            @NonNull SeriesRepository seriesRepository,
            @NonNull EntityState entityState,
            @NonNull SeriesBooksRespository seriesBooksRespository,
            @NonNull List<SeriesItemRelation> seriesItemRelations
    ) {
        super(entityState);
        this.seriesId = seriesId;
        this.user = user;
        this.title = title;
        this.itemsList = new ArrayList<>(itemsList);
        this.seriesBookRelationDTOList = seriesBookRelationDTOList;
        this.seriesRepository = seriesRepository;
        this.seriesBooksRespository = seriesBooksRespository;
        this.seriesItemRelations = new ArrayList<>(seriesItemRelations);
    }

    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    public SeriesDTOv2 toDTO(){

        List<SeriesBookRelationDto> seriesBookRelationDtoList = new ArrayList<>();
        for (SeriesItemRelation seriesItemRelation: seriesItemRelations) {
            if (seriesItemRelation instanceof SeriesBookRelation seriesBookRelation) {
                seriesBookRelationDtoList.add(
                        new SeriesBookRelationDto(
                                seriesBookRelation.seriesId(),
                                seriesBookRelation.bookId(),
                                seriesBookRelation.userId(),
                                (long) seriesItemRelations.indexOf(seriesBookRelation)
                        )
                );
            } else {
                throw new ServerException();
            }
        }

        return new SeriesDTOv2(
                seriesId,
                user.userId(),
                title,
                itemsList.stream()
                        .map(SeriesItem::toDTO)
                        .collect(Collectors.toCollection(ArrayList::new)),
                seriesBookRelationDtoList
        );
    }

    @Override
    public Long getItemsCountAsLong() {
        return (long) itemsList.size();
    }

    @Override
    public void addBookRelation(Long bookId) {
        seriesItemRelations.add(
                new SeriesBookRelation(
                        bookId,
                        seriesId,
                        user.userId()
                )
        );
        entityState = EntityState.DIRTY;
    }

    @Override
    public void removeBookRelation(Long bookId) {
        seriesItemRelations.removeIf( seriesItemRelation -> seriesItemRelation.getBookId().equals(bookId));
        entityState = EntityState.DIRTY;
    }

    @Override
    public SeriesImpl clone() {
        throw new UnsupportedMethodException();
    }

    @Override
    public Long getId() {
        return seriesId;
    }

    @Override
    @Loggable(value = Loggable.DEBUG, prepend = true, trim = false, logThis = true)
    public void save() {
        switch (entityState) {
            case NEW -> seriesRepository.create(toDTO());
            case DIRTY -> seriesRepository.update(persistentCopy.toDTO(), toDTO());
            case PERSISTED -> log.warn("Entity is not altered");
            default -> throw new ServerException("Incorrect entity state");
        }
        entityState = EntityState.PERSISTED;
    }

    @Override
    public void delete() {
        // TODO: Implement
        throw new NotImplementedException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeriesImpl series = (SeriesImpl) o;
        return Objects.equals(seriesId, series.seriesId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(seriesId);
    }

    @Override
    public SeriesImpl deepCopy() {
        return new SeriesImpl(
                seriesId,
                title,
                new ArrayList<>(itemsList),
                user,
                new ArrayList<>(seriesBookRelationDTOList),
                seriesRepository,
                entityState,
                seriesBooksRespository,
                new ArrayList<>(seriesItemRelations)
        );
    }

    /**
     * Set persistent copy
     */
    protected void initPersistentCopy() {
        persistentCopy = deepCopy();
    }
}

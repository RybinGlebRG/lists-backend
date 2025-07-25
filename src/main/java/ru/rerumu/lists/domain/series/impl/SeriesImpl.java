package ru.rerumu.lists.domain.series.impl;

import com.jcabi.aspects.Loggable;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.crosscut.exception.UnsupportedMethodException;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.domain.base.EntityBaseImpl;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.dto.SeriesBookRelationDTO;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesDTOv2;
import ru.rerumu.lists.domain.series.item.SeriesItem;
import ru.rerumu.lists.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ToString
public class SeriesImpl extends EntityBaseImpl implements Series {

    private final Long seriesId;

    @Getter
    private final String title;

    @Getter
    private final List<SeriesItem> itemsList;

    @Getter
    private final User user;

    private final List<SeriesBookRelationDTO> seriesBookRelationDTOList;

    private final SeriesRepository seriesRepository;


    public SeriesImpl(
            Long seriesId,
            @NonNull String title,
            @NonNull List<SeriesItem> itemsList,
            @NonNull User user,
            @NonNull List<SeriesBookRelationDTO> seriesBookRelationDTOList,
            @NonNull SeriesRepository seriesRepository,
            @NonNull EntityState entityState
    ) {
        super(entityState);
        this.seriesId = seriesId;
        this.user = user;
        this.title = title;
        this.itemsList = new ArrayList<>(itemsList);
        this.seriesBookRelationDTOList = seriesBookRelationDTOList;
        this.seriesRepository = seriesRepository;
    }

    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    public SeriesDTOv2 toDTO(){
        return new SeriesDTOv2(
                seriesId,
                user.userId(),
                title,
                itemsList.stream()
                        .map(SeriesItem::toDTO)
                        .collect(Collectors.toCollection(ArrayList::new))
        );
    }

    @Override
    public Long getItemsCountAsLong() {
        return (long) itemsList.size();
    }

    @Override
    public void addBookRelation(Long bookId) {
        throw new NotImplementedException();
    }

    @Override
    public void removeBookRelation(Long bookId) {
        throw new NotImplementedException();
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
    public void save() {
        if (entityState.equals(EntityState.NEW)) {
            seriesRepository.create(toDTO());
        } else if (entityState.equals(EntityState.PERSISTED)) {
            seriesRepository.update(toDTO());
        } else {
            throw new ServerException("Incorrect entity state");
        }
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
}

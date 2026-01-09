package ru.rerumu.lists.domain.series.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesBookRelation;
import ru.rerumu.lists.domain.series.SeriesItemRelation;
import ru.rerumu.lists.domain.series.item.SeriesItem;
import ru.rerumu.lists.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString(callSuper = true, doNotUseGetters = true)
@Slf4j
public class SeriesImpl implements Series {

    private final Long seriesId;

    @Getter
    private final String title;

    @Getter
    private final List<SeriesItem> itemsList;

    @Getter
    private final User user;

    @Getter
    private final List<SeriesItemRelation> seriesItemRelations;


    public SeriesImpl(
            Long seriesId,
            @NonNull String title,
            @NonNull List<SeriesItem> itemsList,
            @NonNull User user,
            @NonNull List<SeriesItemRelation> seriesItemRelations
    ) {
        this.seriesId = seriesId;
        this.user = user;
        this.title = title;
        this.itemsList = new ArrayList<>(itemsList);
        this.seriesItemRelations = new ArrayList<>(seriesItemRelations);
    }

    @Override
    public Long getItemsCountAsLong() {
        return (long) itemsList.size();
    }

    @Override
    public boolean addBookRelation(Long bookId) {
        seriesItemRelations.add(
                new SeriesBookRelation(
                        bookId,
                        seriesId,
                        user.userId()
                )
        );
        return true;
    }

    @Override
    public boolean removeBookRelation(Long bookId) {
        return seriesItemRelations.removeIf( seriesItemRelation -> seriesItemRelation.getItemId().equals(bookId));
    }

    @Override
    public Long getId() {
        return seriesId;
    }

    @Override
    public void save() {
        throw new NotImplementedException();
    }

    @Override
    public void delete() {
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
                new ArrayList<>(seriesItemRelations)
        );
    }
}

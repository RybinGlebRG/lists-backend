package ru.rerumu.lists.dao.series.impl;

import lombok.ToString;
import ru.rerumu.lists.dao.base.EntityState;
import ru.rerumu.lists.dao.base.PersistenceProxy;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesItemRelation;
import ru.rerumu.lists.domain.seriesitem.SeriesItem;
import ru.rerumu.lists.domain.user.User;

import java.util.List;
import java.util.Objects;

@ToString(callSuper = true, doNotUseGetters = true)
public class SeriesPersistenceProxy extends PersistenceProxy<Series> implements Series {

    private final Series series;

    public SeriesPersistenceProxy(Series series, EntityState entityState) {
        super(entityState);
        this.series = series;

        // If persisted, then should have persisted copy
        if (entityState.equals(EntityState.PERSISTED)) {
            initPersistedCopy();
        }
    }

    @Override
    public void initPersistedCopy() {
        persistedCopy = series.deepCopy();
    }

    @Override
    public void clearPersistedCopy() {
        persistedCopy = null;
    }

    @Override
    public String getTitle() {
        return series.getTitle();
    }

    @Override
    public List<SeriesItem> getItemsList() {
        return series.getItemsList();
    }

    @Override
    public Long getItemsCountAsLong() {
        return series.getItemsCountAsLong();
    }

    @Override
    public boolean addBookRelation(Long bookId) {
        boolean isModified = series.addBookRelation(bookId);

        if (isModified) {
            entityState = EntityState.DIRTY;
        }

        return isModified;
    }

    @Override
    public boolean removeBookRelation(Long bookId) {
        boolean isModified = series.removeBookRelation(bookId);

        if (isModified) {
            entityState = EntityState.DIRTY;
        }

        return isModified;
    }

    @Override
    public List<SeriesItemRelation> getSeriesItemRelations() {
        return series.getSeriesItemRelations();
    }

    @Override
    public Long getId() {
        return series.getId();
    }

    @Override
    public User getUser() {
        return series.getUser();
    }

    @Override
    public Series deepCopy() {
        return new SeriesPersistenceProxy(series.deepCopy(), entityState);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SeriesPersistenceProxy that)) return false;
        return Objects.equals(series, that.series);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(series);
    }
}

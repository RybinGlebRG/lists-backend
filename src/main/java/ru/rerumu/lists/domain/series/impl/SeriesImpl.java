package ru.rerumu.lists.domain.series.impl;

import com.jcabi.aspects.Loggable;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.rerumu.lists.crosscut.DeepCopyable;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.crosscut.exception.UnsupportedMethodException;
import ru.rerumu.lists.dao.series.SeriesBooksRespository;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.domain.base.EntityBaseImpl;
import ru.rerumu.lists.domain.base.EntityState;
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
public class SeriesImpl extends EntityBaseImpl<SeriesImpl> implements Series, DeepCopyable<SeriesImpl> {

    private final Long seriesId;

    @Getter
    private final String title;

    @Getter
    private final List<SeriesItem> itemsList;

    @Getter
    private final User user;

    private final SeriesRepository seriesRepository;

    private final SeriesBooksRespository seriesBooksRespository;

    @Getter
    private final List<SeriesItemRelation> seriesItemRelations;


    public SeriesImpl(
            Long seriesId,
            @NonNull String title,
            @NonNull List<SeriesItem> itemsList,
            @NonNull User user,
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
        this.seriesRepository = seriesRepository;
        this.seriesBooksRespository = seriesBooksRespository;
        this.seriesItemRelations = new ArrayList<>(seriesItemRelations);
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
        throw new NotImplementedException();
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
        persistedCopy = deepCopy();
    }
}

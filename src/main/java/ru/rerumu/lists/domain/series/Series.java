package ru.rerumu.lists.domain.series;

import ru.rerumu.lists.crosscut.DeepCopyable;
import ru.rerumu.lists.domain.base.Entity;
import ru.rerumu.lists.domain.seriesitem.SeriesItem;

import java.util.List;

public interface Series extends Entity, DeepCopyable<Series> {

    String getTitle();

    List<SeriesItem> getItemsList();

    Long getItemsCountAsLong();

    /**
     * Add relation between book and series
     */
    boolean addBookRelation(Long bookId);

    /**
     * Remove relation between book and series
     */
    boolean removeBookRelation(Long bookId);


    /*
    ???
     */
    List<SeriesItemRelation> getSeriesItemRelations();
}

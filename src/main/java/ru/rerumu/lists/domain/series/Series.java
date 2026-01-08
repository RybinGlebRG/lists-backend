package ru.rerumu.lists.domain.series;

import ru.rerumu.lists.domain.base.Entity;
import ru.rerumu.lists.domain.series.item.SeriesItem;

import java.util.List;

public interface Series extends Entity {

    String getTitle();

    List<SeriesItem> getItemsList();

    Long getItemsCountAsLong();

    /**
     * Add relation between book and series
     */
    void addBookRelation(Long bookId);

    /**
     * Remove relation between book and series
     */
    void removeBookRelation(Long bookId);


    /*
    ???
     */
    List<SeriesItemRelation> getSeriesItemRelations();
}

package ru.rerumu.lists.repository;

import ru.rerumu.lists.model.Series;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends CrudRepository<Series,Long>{

    Series getOne(Long seriesListId, Long seriesId);
    Optional<Series> getOne(Long seriesId);
    List<Series> getAll(Long seriesListId);
    int getBookCount(Long readListId, Long seriesId);

    long getNextId();
    void add(Series series);

    void delete(long seriesId);
}

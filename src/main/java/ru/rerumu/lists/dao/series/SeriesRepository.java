package ru.rerumu.lists.dao.series;

import ru.rerumu.lists.dao.base.CrudRepository;
import ru.rerumu.lists.model.series.SeriesDTO;

import java.util.List;

public interface SeriesRepository extends CrudRepository<SeriesDTO,Long> {

    @Deprecated
    SeriesDTO getOne(Long seriesListId, Long seriesId);
    List<SeriesDTO> getAll(Long seriesListId);
    int getBookCount(Long readListId, Long seriesId);

//    Integer getNextId();
    void add(SeriesDTO series);

    void delete(long seriesId);
}

package ru.rerumu.lists.mappers;

import ru.rerumu.lists.model.dto.EntityDTO;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.dto.SeriesDTO;

import java.util.List;


public interface SeriesMapper extends CrudMapper<Series,Long, EntityDTO<Series>> {
    SeriesDTO getOne(Long readListId, Long seriesId);
    @Deprecated
    SeriesDTO getOneBySeriesOnly( Long seriesId);
    int getBookCount(Long readListId, Long seriesId);

    long getNextId();

    void add(long readListId, long seriesId, String title);

    void delete(long seriesId);

    List<SeriesDTO> getAll(Long readListId);
}

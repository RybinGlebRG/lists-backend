package ru.rerumu.lists.mappers;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.dto.SeriesDTO;

import java.util.Date;
import java.util.List;


public interface SeriesMapper extends CrudMapper<Series,Long,SeriesDTO> {
    SeriesDTO getOne(Long readListId, Long seriesId);
    @Deprecated
    SeriesDTO getOneBySeriesOnly( Long seriesId);
    int getBookCount(Long readListId, Long seriesId);

    long getNextId();

    void add(long readListId, long seriesId, String title);

    void delete(long seriesId);

    List<SeriesDTO> getAll(Long readListId);
}

package ru.rerumu.lists.mappers;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;

import java.util.Date;
import java.util.List;


public interface SeriesMapper extends CrudMapper<Series,Long> {
    Series getOne(Long readListId, Long seriesId);
    Series getOneBySeriesOnly( Long seriesId);
    List<Series> getAll(Long readListId);
    int getBookCount(Long readListId, Long seriesId);

    long getNextId();

    void add(long readListId, long seriesId, String title);

    void delete(long seriesId);
}

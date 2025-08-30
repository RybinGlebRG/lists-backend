package ru.rerumu.lists.dao.series.mapper;

import lombok.NonNull;
import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.domain.series.SeriesDTO;
import ru.rerumu.lists.domain.series.SeriesDTOv2;

import java.util.List;

@Mapper
public interface SeriesMapper extends CrudMapper<SeriesDTOv2,Long, SeriesDTOv2> {
    SeriesDTO getOne(Long readListId, Long seriesId);
    @Deprecated
    SeriesDTO getOneBySeriesOnly( Long seriesId);
    int getBookCount(Long readListId, Long seriesId);

//    long getNextId();

    void add(long readListId, long seriesId, String title);

    void delete(long seriesId);

    List<SeriesDTO> getAll(Long readListId);

    List<SeriesDTOv2> findByBook(@NonNull Long bookId, @NonNull Long userId);
}

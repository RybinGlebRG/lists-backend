package ru.rerumu.lists.dao.mappers;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.model.series.impl.SeriesImpl;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.model.dto.SeriesBookRelationDTO;

import java.util.List;

@Mapper
public interface SeriesBookMapper {

    void add(Long bookId, Long seriesId, Long readListId, Long seriesOrder);

    void deleteBySeries(Long seriesId);

//    @Deprecated
//    List<SeriesBookRelation> getByBookId(Long bookId);
    List<Long> getSeriesIdsByBookId(Long bookId, Long readListId);
    List<Long> getBookIdsBySeriesId(Long seriesId);

    void update(Long bookId, Long seriesId, Long readListId, Long seriesOrder);

    void delete(Long bookId, Long seriesId, Long readListId);

    @Deprecated
    Long getOrder(Long bookId, Long seriesId, Long readListId);
    Long getOrderByIdOnly(Long bookId, Long seriesId);

    void save(SeriesBookRelation seriesBookRelation);
    void merge(SeriesBookRelation seriesBookRelation);

    List<SeriesBookRelationDTO> findBySeries(SeriesImpl series);
}

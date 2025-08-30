package ru.rerumu.lists.dao.series.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.domain.series.SeriesBookRelationDto;
import ru.rerumu.lists.domain.series.impl.SeriesImpl;
import ru.rerumu.lists.domain.series.SeriesBookRelation;
import ru.rerumu.lists.domain.dto.SeriesBookRelationDTO;

import java.util.List;

@Mapper
public interface SeriesBookMapper {

    void add(Long bookId, Long seriesId, Long readListId, Long seriesOrder);
    void add4User(Long bookId, Long seriesId, Long userId, Long seriesOrder);

    void deleteBySeries(Long seriesId);

    void create(SeriesBookRelation seriesBookRelation);

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
    void merge(SeriesBookRelationDto seriesBookRelationDto);

    List<SeriesBookRelationDTO> findBySeries(SeriesImpl series);
    List<SeriesBookRelationDTO> findBySeriesId(Long seriesId);

    List<SeriesBookRelationDto> getByUserId(Long userId);
}

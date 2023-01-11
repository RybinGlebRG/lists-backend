package ru.rerumu.lists.mappers;

import ru.rerumu.lists.model.SeriesBookRelation;

import java.util.List;

public interface SeriesBookMapper {

    void add(Long bookId, Long seriesId, Long readListId, Long seriesOrder);

    void deleteBySeries(Long seriesId);

    @Deprecated
    List<SeriesBookRelation> getByBookId(Long bookId);
    List<Long> getSeriesIdsByBookId(Long bookId, Long readListId);
    List<Long> getBookIdsBySeriesId(Long seriesId);

    void update(Long bookId, Long seriesId, Long readListId, Long seriesOrder);

    void delete(Long bookId, Long seriesId, Long readListId);

    @Deprecated
    Long getOrder(Long bookId, Long seriesId, Long readListId);
    Long getOrderByIdOnly(Long bookId, Long seriesId);

    void save(SeriesBookRelation seriesBookRelation);
    void merge(SeriesBookRelation seriesBookRelation);
}

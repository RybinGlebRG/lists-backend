package ru.rerumu.lists.dao.repository;

import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.domain.SeriesBookRelation;

import java.util.List;
import java.util.Optional;

public interface SeriesBooksRespository {

    @Deprecated
    void add(Long bookId, Long seriesId, Long readListId, Long seriesOrder);

    void add(SeriesBookRelation seriesBookRelation);

    void deleteBySeries(Long seriesId);

    List<SeriesBookRelation> getByBookId(Long bookId, Long readListId, Long userId);
    List<SeriesBookRelation> getBySeriesId(Long seriesId) throws EntityNotFoundException;

//    List<SeriesBookRelation> getBySeries(Series series) throws EntityNotFoundException, EmptyMandatoryParameterException;

    void update(SeriesBookRelation seriesBookRelation);

    void save(List<SeriesBookRelation> seriesBookRelationList);
    void delete(Long bookId, Long seriesId, Long readListId);

    Optional<SeriesBookRelation> getByIds(Long seriesId, Long bookId) throws EntityNotFoundException;
}

package ru.rerumu.lists.repository;

import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.model.AuthorBookRelation;
import ru.rerumu.lists.model.SeriesBookRelation;

import java.util.List;

public interface SeriesBooksRespository {

    @Deprecated
    void add(Long bookId, Long seriesId, Long readListId, Long seriesOrder);

    void add(SeriesBookRelation seriesBookRelation);

    void deleteBySeries(Long seriesId);

    List<SeriesBookRelation> getByBookId(Long bookId, Long readListId);
    List<SeriesBookRelation> getBySeriesId(Long seriesId) throws EntityNotFoundException;

    void update(SeriesBookRelation seriesBookRelation);
    void delete(Long bookId, Long seriesId, Long readListId);
}

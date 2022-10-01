package ru.rerumu.lists.mappers;

import ru.rerumu.lists.model.SeriesBookRelation;

import java.util.List;

public interface SeriesBookMapper {

    void add(Long bookId, Long seriesId, Long readListId, Long seriesOrder);

    void deleteBySeries(Long seriesId);

    List<SeriesBookRelation> getByBookId(Long bookId);

    void update(Long bookId, Long seriesId, Long readListId, Long seriesOrder);

    void delete(Long bookId, Long seriesId, Long readListId);
}

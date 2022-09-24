package ru.rerumu.lists.repository;

import ru.rerumu.lists.model.AuthorBookRelation;
import ru.rerumu.lists.model.SeriesBookRelation;

import java.util.List;

public interface SeriesBooksRespository {

    void add(Long bookId, Long seriesId, Long readListId, Long seriesOrder);

    void deleteBySeries(Long seriesId);

    List<SeriesBookRelation> getByBookId(Long bookId);
}

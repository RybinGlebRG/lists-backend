package ru.rerumu.lists.mappers;

public interface SeriesBookMapper {

    void add(Long bookId, Long seriesId, Long readListId, Long seriesOrder);
}

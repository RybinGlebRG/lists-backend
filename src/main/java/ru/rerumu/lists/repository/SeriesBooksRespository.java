package ru.rerumu.lists.repository;

public interface SeriesBooksRespository {

    void add(Long bookId, Long seriesId, Long readListId);
}

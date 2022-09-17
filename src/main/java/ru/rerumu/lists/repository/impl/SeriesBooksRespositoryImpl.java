package ru.rerumu.lists.repository.impl;

import ru.rerumu.lists.mappers.SeriesBookMapper;
import ru.rerumu.lists.repository.SeriesBooksRespository;

public class SeriesBooksRespositoryImpl implements SeriesBooksRespository {

    private final SeriesBookMapper seriesBookMapper;

    public SeriesBooksRespositoryImpl(
            SeriesBookMapper seriesBookMapper
    ){
        this.seriesBookMapper = seriesBookMapper;
    }
    @Override
    public void add(Long bookId, Long seriesId, Long readListId, Long seriesOrder) {
        seriesBookMapper.add(bookId, seriesId, readListId, seriesOrder);
    }
}

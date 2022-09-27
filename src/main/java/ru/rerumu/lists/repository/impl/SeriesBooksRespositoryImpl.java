package ru.rerumu.lists.repository.impl;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.mappers.SeriesBookMapper;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.repository.SeriesBooksRespository;

import java.util.List;

@Component
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

    @Override
    public void deleteBySeries(Long seriesId) {
        seriesBookMapper.deleteBySeries(seriesId);
    }

    @Override
    public List<SeriesBookRelation> getByBookId(Long bookId) {
        return seriesBookMapper.getByBookId(bookId);
    }
}

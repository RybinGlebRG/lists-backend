package ru.rerumu.lists.repository.impl;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
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
    public void add(SeriesBookRelation seriesBookRelation) {
        seriesBookMapper.add(
                seriesBookRelation.getBook().getBookId(),
                seriesBookRelation.getSeries().getSeriesId(),
                seriesBookRelation.getBook().getReadListId(),
                seriesBookRelation.getOrder()
        );
    }

    @Override
    public void deleteBySeries(Long seriesId) {
        seriesBookMapper.deleteBySeries(seriesId);
    }

    @Override
    public List<SeriesBookRelation> getByBookId(Long bookId) {
        return seriesBookMapper.getByBookId(bookId);
    }

    @Override
    public void update(SeriesBookRelation seriesBookRelation) {
        seriesBookMapper.update(
                seriesBookRelation.getBook().getBookId(),
                seriesBookRelation.getSeries().getSeriesId(),
                seriesBookRelation.getSeries().getSeriesListId(),
                seriesBookRelation.getOrder()
                );
    }

    @Override
    public void delete(Long bookId, Long seriesId, Long readListId) {
        seriesBookMapper.delete(bookId, seriesId, readListId);
    }
}

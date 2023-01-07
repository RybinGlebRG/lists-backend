package ru.rerumu.lists.repository.impl;

import org.springframework.stereotype.Component;

import ru.rerumu.lists.mappers.SeriesMapper;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.repository.SeriesRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SeriesRepositoryImpl extends CrudRepositoryImpl<Series,Long> implements SeriesRepository{

    private final SeriesMapper seriesMapper;

    public SeriesRepositoryImpl(
            SeriesMapper seriesMapper) {
        super(seriesMapper);
        this.seriesMapper = seriesMapper;
    }

    @Deprecated
    @Override
    public Series getOne(Long readListId, Long seriesId) {

        return seriesMapper.getOne(readListId, seriesId);
    }

    @Override
    public Optional<Series> getOne(Long seriesId) {
        Series series = seriesMapper.findById(seriesId);
        if (series==null){
            return Optional.empty();
        } else {
            return Optional.of(series);
        }
//        List<SeriesBookRelation> seriesBookRelationList = new ArrayList<>();
//        try {
//            seriesBookRelationList = seriesBooksRespository.getBySeriesId(series.getSeriesId());
//        } catch (EntityNotFoundException e){
//            throw new IllegalArgumentException();
//        }
//
//        List<Book> bookList = seriesBookRelationList.stream()
//                .sorted(Comparator.comparingLong(SeriesBookRelation::getOrder))
//                .map(SeriesBookRelation::getBook)
//                .collect(Collectors.toCollection(ArrayList::new));
//
//        Series.Builder seriesBuilder = new Series.Builder(series);
//        seriesBuilder.itemList(bookList);
//
//
//        return Optional.of(seriesBuilder.build());
    }

    @Override
    public List<Series> getAll(Long seriesListId) {
        return seriesMapper.getAll(seriesListId);
    }

    @Override
    public int getBookCount(Long readListId, Long seriesId) {
        return seriesMapper.getBookCount(readListId, seriesId);
    }

    @Override
    public long getNextId() {
        return seriesMapper.getNextId();
    }

    @Override
    public void add(Series series) {
        seriesMapper.add(
                series.getSeriesListId(),
                series.getSeriesId(),
                series.getTitle()
        );
    }

    @Override
    public void delete(long seriesId) {
        seriesMapper.delete(seriesId);
    }
}

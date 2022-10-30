package ru.rerumu.lists.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.mappers.SeriesMapper;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.repository.SeriesRepository;

import java.util.List;
import java.util.Optional;

@Component
public class SeriesRepositoryImpl implements SeriesRepository {

    @Autowired
    private SeriesMapper seriesMapper;

    @Override
    public Series getOne(Long readListId, Long seriesId) {

        return seriesMapper.getOne(readListId, seriesId);
    }

    @Override
    public Optional<Series> getOne(Long seriesId) {
        return Optional.ofNullable(seriesMapper.getOneBySeriesOnly(seriesId));
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

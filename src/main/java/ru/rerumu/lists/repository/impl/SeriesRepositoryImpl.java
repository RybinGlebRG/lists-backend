package ru.rerumu.lists.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.mappers.BookMapper;
import ru.rerumu.lists.mappers.SeriesMapper;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.repository.BookRepository;
import ru.rerumu.lists.repository.SeriesRepository;

import java.util.List;

@Component
public class SeriesRepositoryImpl implements SeriesRepository {

    @Autowired
    private SeriesMapper seriesMapper;

    @Override
    public Series getOne(Long readListId, Long seriesId) {

        return seriesMapper.getOne(readListId, seriesId);
    }

    @Override
    public List<Series> getAll(Long seriesListId) {
        return seriesMapper.getAll(seriesListId);
    }

    @Override
    public int getBookCount(Long readListId, Long seriesId) {
        return seriesMapper.getBookCount(readListId, seriesId);
    }
}

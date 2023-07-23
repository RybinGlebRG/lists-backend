package ru.rerumu.lists.repository.impl;

import org.springframework.stereotype.Component;

import ru.rerumu.lists.mappers.SeriesMapper;
import ru.rerumu.lists.model.*;
import ru.rerumu.lists.model.dto.SeriesDTO;
import ru.rerumu.lists.repository.SeriesRepository;
import ru.rerumu.lists.services.MonitoringService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeriesRepositoryImpl extends CrudRepositoryDtoImpl<Series,Long> implements SeriesRepository{

    private final SeriesMapper seriesMapper;

    public SeriesRepositoryImpl(
            SeriesMapper seriesMapper) {
        super(seriesMapper);
        this.seriesMapper = seriesMapper;
    }

    @Deprecated
    @Override
    public Series getOne(Long readListId, Long seriesId) {

        return seriesMapper.getOne(readListId, seriesId).toSeries();
    }

    @Override
    public List<Series> getAll(Long seriesListId) {

        try {
            List<SeriesDTO> res = seriesMapper.getAll(seriesListId);
            List<Series> resList = res.stream()
                    .map(SeriesDTO::toSeries)
                    .collect(Collectors.toCollection(ArrayList::new));
            return resList;
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getBookCount(Long readListId, Long seriesId) {
        return seriesMapper.getBookCount(readListId, seriesId);
    }

    @Override
    public Long getNextId() {
        return seriesMapper.nextval();
    }

    @Override
    public void add(Series series) {
        seriesMapper.add(
                series.seriesListId(),
                series.seriesId(),
                series.title()
        );
    }

    @Override
    public void delete(long seriesId) {
        seriesMapper.delete(seriesId);
    }
}

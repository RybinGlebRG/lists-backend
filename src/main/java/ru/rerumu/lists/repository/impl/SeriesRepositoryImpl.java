package ru.rerumu.lists.repository.impl;

import org.springframework.stereotype.Component;

import ru.rerumu.lists.mappers.SeriesMapper;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Metric;
import ru.rerumu.lists.model.MetricType;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.dto.SeriesDTO;
import ru.rerumu.lists.repository.SeriesRepository;
import ru.rerumu.lists.services.MonitoringService;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SeriesRepositoryImpl extends CrudRepositoryImpl<Series,Long,SeriesDTO> implements SeriesRepository{

    private final SeriesMapper seriesMapper;
    private final MonitoringService monitoringService = MonitoringService.getServiceInstance();

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
    public Optional<Series> getOne(Long seriesId) {
        SeriesDTO seriesDTO = seriesMapper.findById(seriesId);
        if (seriesDTO==null){
            return Optional.empty();
        } else {
            return Optional.of(seriesDTO.toSeries());
        }
    }

    @Override
    public List<Series> getAll(Long seriesListId) {

        try {
            List<SeriesDTO> res = MonitoringService.gatherExecutionTime(
                    () -> seriesMapper.getAll(seriesListId),
                    MetricType.DB_QUERY__SERIES_MAPPER__GET_ALL__EXECUTION_TIME
            );
            return res.stream()
                    .map(SeriesDTO::toSeries)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e){
            throw new RuntimeException(e);
        }

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
                series.seriesListId(),
                series.seriesId(),
                series.title()
        );
    }

    @Override
    public void delete(long seriesId) {
        seriesMapper.delete(seriesId);
    }

    @Override
    public Optional<Series> findById(Long seriesId) {
        SeriesDTO seriesDTO = seriesMapper.findById(seriesId);
        if (seriesDTO==null){
            return Optional.empty();
        } else {
            return Optional.of(seriesDTO.toSeries());
        }
    }

    @Override
    public List<Series> findAll() {
        throw new RuntimeException("Not Ready");
    }
}

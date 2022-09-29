package ru.rerumu.lists.services;

import org.springframework.stereotype.Service;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.repository.SeriesRepository;

import java.util.Optional;

@Service
public class BookSeriesService {

    private final SeriesRepository seriesRepository;

    public BookSeriesService(
            SeriesRepository seriesRepository
    ) {
        this.seriesRepository = seriesRepository;
    }


    public Optional<Series> getSeries(Long readListId, Long seriesId) {
        if (seriesId == null || readListId == null){
            throw new IllegalArgumentException();
        }
        Series series = seriesRepository.getOne(readListId, seriesId);
        if (series != null) {
            return Optional.of(series);
        } else {
            return Optional.empty();
        }

    }
}

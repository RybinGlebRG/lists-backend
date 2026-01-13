package ru.rerumu.lists.services;

import org.springframework.stereotype.Service;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.domain.series.impl.SeriesImpl;
import ru.rerumu.lists.domain.series.SeriesBookRelation;
import ru.rerumu.lists.dao.series.SeriesBooksRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class BookSeriesRelationService {

    private final SeriesBooksRepository seriesBooksRepository;

    public BookSeriesRelationService(
            SeriesBooksRepository seriesBooksRepository
    ) {
        this.seriesBooksRepository = seriesBooksRepository;
    }


    public void update(SeriesBookRelation seriesBookRelation) {
        seriesBooksRepository.update(seriesBookRelation);
    }

    public void delete(Long bookId, Long seriesId, Long readListId) {
        seriesBooksRepository.delete(bookId, seriesId, readListId);
    }

    public List<SeriesBookRelation> getByBookId(Long bookId, Long readListId, Long userId) {
        return seriesBooksRepository.getByBookId(bookId, readListId, userId);
    }

    public List<SeriesBookRelation> getBySeries(Long seriesId) throws EntityNotFoundException {
        return seriesBooksRepository.getBySeriesId(seriesId);
    }

    public List<SeriesBookRelation> getBySeries(SeriesImpl series) throws EntityNotFoundException {
        return seriesBooksRepository.getBySeriesId(series.getId());
    }

    public HashMap<SeriesImpl, List<SeriesBookRelation>> get(List<SeriesImpl> seriesList) {
        HashMap<SeriesImpl, List<SeriesBookRelation>> seriesListHashMap = new HashMap<>();
        for (SeriesImpl series : seriesList) {
            try {
                seriesListHashMap.put(series, getBySeries(series));
            } catch (EntityNotFoundException e) {
                seriesListHashMap.put(series, new ArrayList<>());
            }
        }
        return seriesListHashMap;
    }
}

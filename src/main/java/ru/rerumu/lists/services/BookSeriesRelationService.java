package ru.rerumu.lists.services;

import org.springframework.stereotype.Service;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.domain.series.impl.SeriesImpl;
import ru.rerumu.lists.domain.SeriesBookRelation;
import ru.rerumu.lists.dao.repository.SeriesBooksRespository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class BookSeriesRelationService {

    private final SeriesBooksRespository seriesBooksRespository;

    public BookSeriesRelationService(
            SeriesBooksRespository seriesBooksRespository
    ) {
        this.seriesBooksRespository = seriesBooksRespository;
    }


    public void update(SeriesBookRelation seriesBookRelation) {
        seriesBooksRespository.update(seriesBookRelation);
    }

    public void add(SeriesBookRelation seriesBookRelation) {
        seriesBooksRespository.add(seriesBookRelation);
    }

    public void delete(Long bookId, Long seriesId, Long readListId) {
        seriesBooksRespository.delete(bookId, seriesId, readListId);
    }

    public List<SeriesBookRelation> getByBookId(Long bookId, Long readListId, Long userId) {
        return seriesBooksRespository.getByBookId(bookId, readListId, userId);
    }

    public List<SeriesBookRelation> getBySeries(Long seriesId) throws EntityNotFoundException {
        return seriesBooksRespository.getBySeriesId(seriesId);
    }

    public List<SeriesBookRelation> getBySeries(SeriesImpl series) throws EntityNotFoundException {
        return seriesBooksRespository.getBySeriesId(series.getId());
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

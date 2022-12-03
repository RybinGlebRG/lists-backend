package ru.rerumu.lists.services;

import org.springframework.stereotype.Service;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.repository.SeriesBooksRespository;

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

    public List<SeriesBookRelation> getByBookId(Long bookId, Long readListId) {
        return seriesBooksRespository.getByBookId(bookId, readListId);
    }

    public List<SeriesBookRelation> getBySeries(Long seriesId) throws EntityNotFoundException {
        return seriesBooksRespository.getBySeriesId(seriesId);
    }

    public List<SeriesBookRelation> getBySeries(Series series) throws EntityNotFoundException {
        return seriesBooksRespository.getBySeriesId(series.getSeriesId());
    }

    public HashMap<Series, List<SeriesBookRelation>> get(List<Series> seriesList) {
        HashMap<Series, List<SeriesBookRelation>> seriesListHashMap = new HashMap<>();
        for (Series series : seriesList) {
            try {
                seriesListHashMap.put(series, getBySeries(series));
            } catch (EntityNotFoundException e) {
                seriesListHashMap.put(series, new ArrayList<>());
            }
        }
        return seriesListHashMap;
    }
}

package ru.rerumu.lists.services;

import org.springframework.stereotype.Service;
import ru.rerumu.lists.exception.EntityHasChildrenException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.repository.SeriesRepository;
import ru.rerumu.lists.views.BookSeriesAddView;

import java.util.List;
import java.util.Optional;

@Service
public class BookSeriesService {

    private final SeriesRepository seriesRepository;

    private final BookSeriesRelationService bookSeriesRelationService;

    public BookSeriesService(
            SeriesRepository seriesRepository,
            BookSeriesRelationService bookSeriesRelationService
    ) {
        this.seriesRepository = seriesRepository;
        this.bookSeriesRelationService = bookSeriesRelationService;
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

    public void add(long readListId, BookSeriesAddView bookSeriesAddView){
        long nextId = seriesRepository.getNextId();

        Series series = new Series.Builder()
                .seriesId(nextId)
                .title(bookSeriesAddView.getTitle())
                .readListId(readListId)
                .build();

        seriesRepository.add(series);
    }

    public void delete(long seriesId) throws EntityNotFoundException, EntityHasChildrenException {

        Optional<Series> optionalSeries = seriesRepository.getOne(seriesId);

        if (optionalSeries.isEmpty()){
            throw new EntityNotFoundException();
        }

        List<SeriesBookRelation> seriesBookRelationList = bookSeriesRelationService.getBySeriesId(seriesId);
        if (seriesBookRelationList.size() >0){
            throw new EntityHasChildrenException();
        }

        seriesRepository.delete(seriesId);
    }
}

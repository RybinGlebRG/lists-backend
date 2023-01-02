package ru.rerumu.lists.services;

import org.springframework.stereotype.Service;
import ru.rerumu.lists.exception.EntityHasChildrenException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.repository.SeriesRepository;
import ru.rerumu.lists.views.BookSeriesAddView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SeriesService {

    private final SeriesRepository seriesRepository;

    private final BookSeriesRelationService bookSeriesRelationService;

    public SeriesService(
            SeriesRepository seriesRepository,
            BookSeriesRelationService bookSeriesRelationService
    ) {
        this.seriesRepository = seriesRepository;
        this.bookSeriesRelationService = bookSeriesRelationService;
    }


    public Optional<Series> getSeries(Long readListId, Long seriesId) {
        if (seriesId == null || readListId == null) {
            throw new IllegalArgumentException();
        }
        Optional<Series> series = seriesRepository.getOne(seriesId);
        return series;
    }

    public void add(long readListId, BookSeriesAddView bookSeriesAddView) {
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

        if (optionalSeries.isEmpty()) {
            throw new EntityNotFoundException();
        }

        List<SeriesBookRelation> seriesBookRelationList = bookSeriesRelationService.getBySeries(seriesId);
        if (seriesBookRelationList.size() > 0) {
            throw new EntityHasChildrenException();
        }

        seriesRepository.delete(seriesId);
    }

//    private Optional<LocalDateTime> getBookLastUpdate(Series series){
//        try {
//            List<SeriesBookRelation> seriesBookRelationList = bookSeriesRelationService.getBySeries(series.getSeriesId());
//
//            Comparator<SeriesBookRelation> lastUpdateDateComparator = Comparator
//                    .comparing((SeriesBookRelation seriesBookRelation) -> seriesBookRelation.getBook().getLastUpdateDate_V2())
//                    .reversed()
//                    .thenComparing(seriesBookRelation -> seriesBookRelation.getBook().getTitle())
//                    .thenComparing(seriesBookRelation -> seriesBookRelation.getBook().getBookId());
//
//            Optional<SeriesBookRelation> optionalSeriesBookRelation = seriesBookRelationList.stream().min(lastUpdateDateComparator);
//
//            if (optionalSeriesBookRelation.isPresent()){
//                return Optional.of(optionalSeriesBookRelation.get().getBook().getLastUpdateDate_V2());
//            }
//
//        } catch (EntityNotFoundException ignored) {
//        }
//        return Optional.empty();
//    }


    public List<Series> getAll(Long readListId) {

        return seriesRepository.getAll(readListId);
    }
}

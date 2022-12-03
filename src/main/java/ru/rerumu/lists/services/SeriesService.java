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
        Series series = seriesRepository.getOne(readListId, seriesId);
        if (series != null) {
            return Optional.of(series);
        } else {
            return Optional.empty();
        }

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

    /* TODO: Test
        1. no book
        2. ordering

     */
    public List<Series> getAll(Long readListId) {
        List<Series> res = new ArrayList<>();
        List<Series> seriesList = seriesRepository.getAll(readListId);

        for (Series item : seriesList) {
            int bookCount = seriesRepository.getBookCount(item.getSeriesListId(), item.getSeriesId());
//            Optional<LocalDateTime> bookLastUpdate = getBookLastUpdate(item);

            Series.Builder seriesBuilder = new Series.Builder(item).bookCount(bookCount);

//            bookLastUpdate.ifPresent(seriesBuilder::lastUpdateDate);

//            if (bookLastUpdate.isPresent()){
//                seriesBuilder.lastUpdateDate(bookLastUpdate.get());
//            } else {
//                seriesBuilder.lastUpdateDate(LocalDateTime.MIN);
//            }

            res.add(seriesBuilder.build());

        }
        return res;
    }
}

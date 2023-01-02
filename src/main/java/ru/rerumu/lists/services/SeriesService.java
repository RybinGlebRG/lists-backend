package ru.rerumu.lists.services;

import org.springframework.stereotype.Service;
import ru.rerumu.lists.exception.EntityHasChildrenException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.repository.SeriesBooksRespository;
import ru.rerumu.lists.repository.SeriesRepository;
import ru.rerumu.lists.views.BookSeriesAddView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeriesService {

    private final SeriesRepository seriesRepository;

    private final BookSeriesRelationService bookSeriesRelationService;

    private final SeriesBooksRespository seriesBooksRespository;

    public SeriesService(
            SeriesRepository seriesRepository,
            BookSeriesRelationService bookSeriesRelationService,
            SeriesBooksRespository seriesBooksRespository
    ) {
        this.seriesRepository = seriesRepository;
        this.bookSeriesRelationService = bookSeriesRelationService;
        this.seriesBooksRespository = seriesBooksRespository;
    }


    public Optional<Series> getSeries(Long seriesId) {
        Optional<Series> optionalSeries = seriesRepository.getOne(seriesId);
        if (optionalSeries.isPresent()) {
            Series.Builder seriesBuilder = new Series.Builder(optionalSeries.get());
            try {
                List<Book> bookList = seriesBooksRespository.getBySeriesId(optionalSeries.get().getSeriesId()).stream()
                        .map(SeriesBookRelation::getBook)
                        .collect(Collectors.toCollection(ArrayList::new));
                seriesBuilder.itemList(bookList);
            } catch (EntityNotFoundException e){
                throw new IllegalArgumentException();
            }

            return Optional.of(seriesBuilder.build());
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


    public List<Series> getAll(Long readListId) {

        return seriesRepository.getAll(readListId);
    }

    public List<Series> findByBook(Book book){
        return seriesBooksRespository.getByBookId(book.getBookId(), book.getReadListId()).stream()
                .map(SeriesBookRelation::getSeries)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}

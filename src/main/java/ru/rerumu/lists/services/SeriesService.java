package ru.rerumu.lists.services;

import org.springframework.stereotype.Service;
import ru.rerumu.lists.exception.EntityHasChildrenException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.model.SeriesItemType;
import ru.rerumu.lists.repository.SeriesBooksRespository;
import ru.rerumu.lists.repository.SeriesRepository;
import ru.rerumu.lists.views.BookSeriesAddView;
import ru.rerumu.lists.views.series_update.SeriesUpdateItem;
import ru.rerumu.lists.views.series_update.SeriesUpdateView;

import java.util.*;
import java.util.function.Function;
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
            } catch (EntityNotFoundException e) {
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

    public List<Series> getAll(Long readListId) {

        return seriesRepository.getAll(readListId);
    }

//    private SeriesBookRelation updateBookOrder(Long bookId, Long seriesId, Long order) throws EntityNotFoundException {
//        Optional<SeriesBookRelation> optionalSeriesBookRelation = seriesBooksRespository
//                .getByIds(seriesId, bookId);
//        // TODO: Check absent in update view
//        // TODO: Check ordering is not sparse
//        if (optionalSeriesBookRelation.isEmpty()) {
//            // TODO: Add relation
//            throw new EntityNotFoundException();
//        } else if (!optionalSeriesBookRelation.get().getOrder().equals(order)) {
//            SeriesBookRelation seriesBookRelation = new SeriesBookRelation(
//                    optionalSeriesBookRelation.get().getBook(),
//                    optionalSeriesBookRelation.get().getSeries(),
//                    order
//            );
//            return new SeriesBookRelation(
//                    optionalSeriesBookRelation.get().getBook(),
//                    optionalSeriesBookRelation.get().getSeries(),
//                    order
//            );
////            seriesBooksRespository.update(seriesBookRelation);
//        }
//        return null;
//    }

    public void updateSeries(Long seriesId, SeriesUpdateView seriesUpdateView) throws EntityNotFoundException {
        List<SeriesBookRelation> seriesBookRelationList = seriesBooksRespository.getBySeriesId(seriesId);
        // TODO: Remove this limitation
        if (seriesBookRelationList.size() != seriesUpdateView.itemList().size()) {
            throw new IllegalArgumentException();
        }

        Map<Long, SeriesBookRelation> relationMap = seriesBookRelationList.stream()
                .collect(Collectors.toMap(
                        seriesBookRelation -> seriesBookRelation.getBook().getBookId(),
                        Function.identity(),
                        (o1, o2) -> {
                            throw new AssertionError();
                        },
                        HashMap::new
                ));

        List<SeriesBookRelation> updatedSeriesBookRelations = new ArrayList<>();

        for (int i = 0; i < seriesUpdateView.itemList().size(); i++) {

            switch (seriesUpdateView.itemList().get(i).itemType()) {
                case BOOK -> {
                    Optional<SeriesBookRelation> optionalRelation = Optional.ofNullable(relationMap.get(
                            seriesUpdateView.itemList().get(i).itemId()
                    ));

                    // TODO: Remove this limitation
                    optionalRelation.orElseThrow(AssertionError::new);

                    if (optionalRelation.get().getOrder() != i) {
                        updatedSeriesBookRelations.add(
                                new SeriesBookRelation(
                                        optionalRelation.get().getBook(),
                                        optionalRelation.get().getSeries(),
                                        (long) i
                                )
                        );
                    }

                }
                default -> throw new IllegalArgumentException();
            }
        }

//        for (int i = 0; i < seriesUpdateView.itemList().size(); i++) {
//
//            switch (seriesUpdateView.itemList().get(i).itemType()) {
//                case BOOK -> updateBookOrder(
//                        seriesUpdateView.itemList().get(i).itemId(),
//                        seriesId,
//                        (long) i
//                );
//                default -> throw new IllegalArgumentException();
//            }
//        }
        // TODO: Perform update
    }


}

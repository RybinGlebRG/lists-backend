package ru.rerumu.lists.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.exception.EntityHasChildrenException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.SeriesBookRelation;
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

                List<SeriesBookRelation> relationList = seriesBooksRespository.getBySeriesId(
                        optionalSeries.get().getSeriesId()
                );
                relationList.sort(Comparator.comparingLong(SeriesBookRelation::order));

                List<Book> bookList = relationList.stream()
                        .map(SeriesBookRelation::book)
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

    @Transactional(rollbackFor = Exception.class)
    public void add(long readListId, BookSeriesAddView bookSeriesAddView) {
        long nextId = seriesRepository.getNextId();

        Series series = new Series.Builder()
                .seriesId(nextId)
                .title(bookSeriesAddView.getTitle())
                .readListId(readListId)
                .build();

        seriesRepository.add(series);
    }

    @Transactional(rollbackFor = Exception.class)
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

    @Transactional(rollbackFor = Exception.class)
    public void updateSeries(Long seriesId, SeriesUpdateView seriesUpdateView) throws EntityNotFoundException {
        List<SeriesBookRelation> seriesBookRelationList = seriesBooksRespository.getBySeriesId(seriesId);

//        Optional<Series> series = getSeries(seriesId);
//        series.orElseThrow(EntityNotFoundException::new);

        // TODO: Remove this limitation
        if (seriesBookRelationList.size() != seriesUpdateView.itemList().size()) {
            throw new IllegalArgumentException();
        }

        Map<Long, SeriesBookRelation> relationMap = seriesBookRelationList.stream()
                .collect(Collectors.toMap(
                        seriesBookRelation -> seriesBookRelation.book().getBookId(),
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

                    if (optionalRelation.get().order() != i) {
                        updatedSeriesBookRelations.add(
                                new SeriesBookRelation(
                                        optionalRelation.get().book(),
                                        optionalRelation.get().series(),
                                        (long) i
                                )
                        );
                    }

                }
                default -> throw new IllegalArgumentException();
            }
        }
        seriesBooksRespository.save(updatedSeriesBookRelations);
    }


}

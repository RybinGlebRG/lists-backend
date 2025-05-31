package ru.rerumu.lists.services.series.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.dao.repository.SeriesBooksRespository;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.crosscut.exception.EntityHasChildrenException;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.model.book.Book;
import ru.rerumu.lists.model.book.impl.BookImpl;
import ru.rerumu.lists.model.series.Series;
import ru.rerumu.lists.model.series.SeriesFactory;
import ru.rerumu.lists.model.series.item.SeriesItem;
import ru.rerumu.lists.services.BookSeriesRelationService;
import ru.rerumu.lists.services.book.impl.ReadListService;
import ru.rerumu.lists.services.series.SeriesService;
import ru.rerumu.lists.views.BookSeriesAddView;
import ru.rerumu.lists.views.seriesupdate.SeriesUpdateItem;
import ru.rerumu.lists.views.seriesupdate.SeriesUpdateView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class SeriesServiceImpl implements SeriesService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SeriesRepository seriesRepository;

    private final BookSeriesRelationService bookSeriesRelationService;

    private final SeriesBooksRespository seriesBooksRespository;

    private final ReadListService readListService;
    private final SeriesFactory seriesFactory;

    public SeriesServiceImpl(
            SeriesRepository seriesRepository,
            BookSeriesRelationService bookSeriesRelationService,
            SeriesBooksRespository seriesBooksRespository,
            ReadListService readListService, SeriesFactory seriesFactory
    ) {
        this.seriesRepository = seriesRepository;
        this.bookSeriesRelationService = bookSeriesRelationService;
        this.seriesBooksRespository = seriesBooksRespository;
        this.readListService = readListService;
        this.seriesFactory = seriesFactory;
    }


    public Optional<Series> getSeries(Long seriesId) {
        return seriesRepository.findById(seriesId).map(seriesFactory::fromDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(long readListId, BookSeriesAddView bookSeriesAddView) {
        long nextId = seriesRepository.getNextId();

        Series series = new Series.Builder()
                .seriesId(nextId)
                .title(bookSeriesAddView.getTitle())
                .readListId(readListId)
                .build();

        seriesRepository.add(series.toDTO());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(long seriesId) throws EntityNotFoundException, EntityHasChildrenException {

        Optional<Series> optionalSeries = seriesRepository.findById(seriesId).map(seriesFactory::fromDTO);

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
        return seriesRepository.getAll(readListId).stream()
                .map(seriesFactory::fromDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Deprecated
    public Map<BookImpl, List<Series>> findByBook(List<BookImpl> bookList) {
        Map<BookImpl, List<Series>> bookSeriesMap = new HashMap<>();
        for (BookImpl book : bookList) {
            List<SeriesBookRelation> seriesBookRelationList = seriesBooksRespository.getByBookId(book.getBookId(), book.getReadListId());
            List<Series> seriesList = seriesBookRelationList.stream()
                    .map(SeriesBookRelation::series)
                    .collect(Collectors.toCollection(ArrayList::new));
            bookSeriesMap.put(book, seriesList);
        }
        return bookSeriesMap;
    }

    public List<Series> findByBook(BookImpl book) {
        List<SeriesBookRelation> seriesBookRelationList = seriesBooksRespository.getByBookId(book.getBookId(), book.getReadListId());
        return seriesBookRelationList.stream()
                .map(SeriesBookRelation::series)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void removeBookRelations(Series source, Series target) {
        List<SeriesBookRelation> relationsToRemove = source.getItemsList().stream()
                .filter(o1 -> o1 instanceof BookImpl)
                .map(o1 -> (BookImpl) o1)
                .filter(b1 -> {
                    for (Object o2 : target.getItemsList()) {
                        if (o2 instanceof BookImpl b2 && b1.equals(b2)) {
                            return false;
                        }
                    }
                    return true;
                })
                .map(b1 -> {
                    try {
                        return seriesBooksRespository.getByIds(source.getSeriesId(), b1.getBookId());
                    } catch (EntityNotFoundException e) {
                        // Supposed to be present
                        throw new AssertionError(e);
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toCollection(ArrayList::new));

        logger.debug("Relations to remove: " + relationsToRemove);

        for (SeriesBookRelation seriesBookRelation : relationsToRemove) {
            seriesBooksRespository.delete(
                    seriesBookRelation.book().getId(),
                    seriesBookRelation.series().getSeriesId(),
                    seriesBookRelation.series().getSeriesListId()
            );
        }
    }

    private void saveBookRelations(Series series) {
        List<BookImpl> booksList = series.getItemsList().stream()
                .filter(item -> item instanceof BookImpl)
                .map(item -> (BookImpl) item)
                .collect(Collectors.toCollection(ArrayList::new));
        List<SeriesBookRelation> bookRelations = IntStream.range(0, booksList.size())
                .mapToObj(i -> new SeriesBookRelation(booksList.get(i), series, (long) i + 1))
                .collect(Collectors.toCollection(ArrayList::new));
        logger.debug("saveBookRelations: " + bookRelations);
        seriesBooksRespository.save(bookRelations);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateSeries(Long seriesId, SeriesUpdateView seriesUpdateView) throws EntityNotFoundException, EmptyMandatoryParameterException {
        logger.debug(seriesUpdateView.toString());

        Optional<Series> series = getSeries(seriesId);
        series.orElseThrow(EntityNotFoundException::new);

        List<SeriesItem> updatedItems = new ArrayList<>();
        for (SeriesUpdateItem seriesUpdateItem : seriesUpdateView.itemList()) {
            switch (seriesUpdateItem.itemType()) {
                case BOOK -> {
                    Book book = readListService.getBook(seriesUpdateItem.itemId());
                    updatedItems.add(book);
                }
                default -> throw new IllegalArgumentException();
            }
        }
        logger.debug("updatedItems: " + updatedItems);

        Series updatedSeries = new Series.Builder(series.get())
                .itemList(updatedItems)
                .build();

        removeBookRelations(series.get(), updatedSeries);
        saveBookRelations(updatedSeries);


    }


}

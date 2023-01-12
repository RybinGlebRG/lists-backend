package ru.rerumu.lists.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.stream.IntStream;

@Service
public class SeriesService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SeriesRepository seriesRepository;

    private final BookSeriesRelationService bookSeriesRelationService;

    private final SeriesBooksRespository seriesBooksRespository;

    private final ReadListService readListService;

    public SeriesService(
            SeriesRepository seriesRepository,
            BookSeriesRelationService bookSeriesRelationService,
            SeriesBooksRespository seriesBooksRespository,
            ReadListService readListService
    ) {
        this.seriesRepository = seriesRepository;
        this.bookSeriesRelationService = bookSeriesRelationService;
        this.seriesBooksRespository = seriesBooksRespository;
        this.readListService = readListService;
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

    public Map<Book,List<Series>> findByBook(List<Book> bookList){
        Map<Book,List<Series>> bookSeriesMap = new HashMap<>();
        for (Book book: bookList){
            List<SeriesBookRelation> seriesBookRelationList = seriesBooksRespository.getByBookId(book.getBookId(), book.getReadListId());
            List<Series> seriesList = seriesBookRelationList.stream()
                    .map(SeriesBookRelation::series)
                    .collect(Collectors.toCollection(ArrayList::new));
            bookSeriesMap.put(book,seriesList);
        }
        return bookSeriesMap;
    }

    public List<Series> findByBook(Book book){
            List<SeriesBookRelation> seriesBookRelationList = seriesBooksRespository.getByBookId(book.getBookId(), book.getReadListId());
            List<Series> seriesList = seriesBookRelationList.stream()
                    .map(SeriesBookRelation::series)
                    .collect(Collectors.toCollection(ArrayList::new));
            return seriesList;
    }

    private void removeBookRelations(Series source, Series target){
        List<SeriesBookRelation> relationsToRemove = source.getItemsList().stream()
                .filter(o1 ->o1 instanceof Book)
                .map(o1 -> (Book)o1)
                .filter(b1 -> {
                    for (Object o2: target.getItemsList()){
                        if (o2 instanceof Book b2 && b1.equals(b2)){
                            return false;
                        }
                    }
                    return true;
                })
                .map(b1->{
                    try {
                        return  seriesBooksRespository.getByIds(source.getSeriesId(), b1.getBookId());
                    } catch (EntityNotFoundException e) {
                        // Supposed to be present
                        throw new AssertionError(e);
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toCollection(ArrayList::new));

        logger.debug("Relations to remove: "+relationsToRemove);

        for(SeriesBookRelation seriesBookRelation: relationsToRemove){
            seriesBooksRespository.delete(
                    seriesBookRelation.book().getBookId(),
                    seriesBookRelation.series().getSeriesId(),
                    seriesBookRelation.series().getSeriesListId()
            );
        }
    }

    private void saveBookRelations(Series series){
        List<Book> booksList = series.getItemsList().stream()
                .filter(item->item instanceof Book)
                .map(item-> (Book) item)
                .collect(Collectors.toCollection(ArrayList::new));
        List<SeriesBookRelation> bookRelations = IntStream.range(0,booksList.size())
                .mapToObj(i-> new SeriesBookRelation(booksList.get(i),series,(long)i+1))
                .collect(Collectors.toCollection(ArrayList::new));
        logger.debug("saveBookRelations: "+bookRelations);
        seriesBooksRespository.save(bookRelations);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateSeries(Long seriesId, SeriesUpdateView seriesUpdateView) throws EntityNotFoundException {
        logger.debug(seriesUpdateView.toString());

        Optional<Series> series = getSeries(seriesId);
        series.orElseThrow(EntityNotFoundException::new);

        List<Object> updatedItems = new ArrayList<>();
        for (SeriesUpdateItem seriesUpdateItem: seriesUpdateView.itemList()){
            switch (seriesUpdateItem.itemType()){
                case BOOK -> {
                    Optional<Book> optionalBook = readListService.getBook(seriesUpdateItem.itemId());
                    optionalBook.orElseThrow(EntityNotFoundException::new);
                    optionalBook.ifPresent(updatedItems::add);
                }
                default -> throw new IllegalArgumentException();
            }
        }
        logger.debug("updatedItems: "+updatedItems);

        Series updatedSeries = new Series.Builder(series.get())
                .itemList(updatedItems)
                .build();

        removeBookRelations(series.get(), updatedSeries);
        saveBookRelations(updatedSeries);


    }


}

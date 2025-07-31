package ru.rerumu.lists.services.series.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.controller.series.view.in.SeriesUpdateItem;
import ru.rerumu.lists.controller.series.view.in.SeriesUpdateView;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.dao.series.SeriesBooksRespository;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.domain.SeriesBookRelation;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.book.impl.BookImpl;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.domain.series.impl.SeriesImpl;
import ru.rerumu.lists.domain.series.item.SeriesItem;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserFactory;
import ru.rerumu.lists.services.BookSeriesRelationService;
import ru.rerumu.lists.services.book.impl.ReadListService;
import ru.rerumu.lists.services.series.SeriesService;
import ru.rerumu.lists.views.BookSeriesAddView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class SeriesServiceImpl implements SeriesService {
    private final SeriesRepository seriesRepository;

    private final BookSeriesRelationService bookSeriesRelationService;

    private final SeriesBooksRespository seriesBooksRespository;

    private final ReadListService readListService;
    private final SeriesFactory seriesFactory;
    private final UserFactory userFactory;

    public SeriesServiceImpl(
            SeriesRepository seriesRepository,
            BookSeriesRelationService bookSeriesRelationService,
            SeriesBooksRespository seriesBooksRespository,
            ReadListService readListService,
            SeriesFactory seriesFactory, UserFactory userFactory
    ) {
        this.seriesRepository = seriesRepository;
        this.bookSeriesRelationService = bookSeriesRelationService;
        this.seriesBooksRespository = seriesBooksRespository;
        this.readListService = readListService;
        this.seriesFactory = seriesFactory;
        this.userFactory = userFactory;
    }

    @Override
    public List<Series> findAll(@NonNull Long userId) {
        User user = userFactory.findById(userId);
        return seriesFactory.findAll(user);
    }

    @Override
    public Series findById(@NonNull Long seriesId, @NonNull Long userId) {
        User user = userFactory.findById(userId);
        return seriesFactory.findById(user, seriesId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Series add(Long userId, BookSeriesAddView bookSeriesAddView) {
        User user = userFactory.findById(userId);
        Long nextId = seriesRepository.getNextId();
        Series series = seriesFactory.buildSeries(nextId, bookSeriesAddView.getTitle(), user);

        series.save();

        return series;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long seriesId, Long userId) {
        User user = userFactory.findById(userId);
        Optional<SeriesImpl> optionalSeries = seriesRepository.findById(seriesId, user).map(seriesFactory::fromDTOv2);

        if (optionalSeries.isEmpty()) {
            throw new EntityNotFoundException();
        }

        List<SeriesBookRelation> seriesBookRelationList = bookSeriesRelationService.getBySeries(seriesId);
        if (seriesBookRelationList.size() > 0) {
            throw new ServerException("EntityHasChildrenException");
//            throw new EntityHasChildrenException();
        }

        seriesRepository.delete(seriesId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSeries(Long seriesId, Long userId, SeriesUpdateView seriesUpdateView) {
        log.debug(seriesUpdateView.toString());

        Series series = findById(seriesId, userId);

        List<SeriesItem> updatedItems = new ArrayList<>();
        for (SeriesUpdateItem seriesUpdateItem : seriesUpdateView.itemList()) {
            switch (seriesUpdateItem.itemType()) {
                case BOOK -> {
                    Book book = readListService.getBook(seriesUpdateItem.itemId(), userId);
                    updatedItems.add(book);
                }
                default -> throw new IllegalArgumentException();
            }
        }
        log.debug("updatedItems: " + updatedItems);

        throw new NotImplementedException();
//        Series updatedSeries = new SeriesImpl(
//                series.getId(),
//                series.getTitle(),
//                series.getItemsList(),
//                series.getUser(),
//        );
//
//        removeBookRelations((SeriesImpl) series, (SeriesImpl) updatedSeries);
//        saveBookRelations((SeriesImpl) updatedSeries);
    }

    @Deprecated
    public Map<BookImpl, List<SeriesImpl>> findByBook(List<BookImpl> bookList, Long userId) {
        Map<BookImpl, List<SeriesImpl>> bookSeriesMap = new HashMap<>();
        for (BookImpl book : bookList) {
            List<SeriesBookRelation> seriesBookRelationList = seriesBooksRespository.getByBookId(book.getBookId(), book.getReadListId(), userId);
            List<SeriesImpl> seriesList = seriesBookRelationList.stream()
                    .map(SeriesBookRelation::series)
                    .collect(Collectors.toCollection(ArrayList::new));
            bookSeriesMap.put(book, seriesList);
        }
        return bookSeriesMap;
    }

    // TODO: fix null
    @Override
    public List<Series> findByBook(Book book, Long userId) {
        List<SeriesBookRelation> seriesBookRelationList = seriesBooksRespository.getByBookId(book.getId(), null, userId);
        return seriesBookRelationList.stream()
                .map(SeriesBookRelation::series)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void removeBookRelations(SeriesImpl source, SeriesImpl target) {
        throw new NotImplementedException();
//        List<SeriesBookRelation> relationsToRemove = source.getItemsList().stream()
//                .filter(o1 -> o1 instanceof BookImpl)
//                .map(o1 -> (BookImpl) o1)
//                .filter(b1 -> {
//                    for (Object o2 : target.getItemsList()) {
//                        if (o2 instanceof BookImpl b2 && b1.equals(b2)) {
//                            return false;
//                        }
//                    }
//                    return true;
//                })
//                .map(b1 -> {
//                    try {
//                        return seriesBooksRespository.getByIds(source.getSeriesId(), b1.getBookId());
//                    } catch (EntityNotFoundException e) {
//                        // Supposed to be present
//                        throw new AssertionError(e);
//                    }
//                })
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .collect(Collectors.toCollection(ArrayList::new));
//
//        log.debug("Relations to remove: " + relationsToRemove);
//
//        for (SeriesBookRelation seriesBookRelation : relationsToRemove) {
//            seriesBooksRespository.delete(
//                    seriesBookRelation.book().getId(),
//                    seriesBookRelation.series().getSeriesId(),
//                    seriesBookRelation.series().getSeriesListId()
//            );
//        }
    }

    private void saveBookRelations(SeriesImpl series) {
        List<BookImpl> booksList = series.getItemsList().stream()
                .filter(item -> item instanceof BookImpl)
                .map(item -> (BookImpl) item)
                .collect(Collectors.toCollection(ArrayList::new));
        List<SeriesBookRelation> bookRelations = IntStream.range(0, booksList.size())
                .mapToObj(i -> new SeriesBookRelation(booksList.get(i), series, (long) i + 1))
                .collect(Collectors.toCollection(ArrayList::new));
        log.debug("saveBookRelations: " + bookRelations);
        seriesBooksRespository.save(bookRelations);
    }
}

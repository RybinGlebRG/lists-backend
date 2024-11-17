package ru.rerumu.lists.repository.impl;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.mappers.SeriesBookMapper;
import ru.rerumu.lists.model.book.BookImpl;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.repository.BookRepository;
import ru.rerumu.lists.repository.SeriesBooksRespository;
import ru.rerumu.lists.repository.SeriesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class SeriesBooksRespositoryImpl implements SeriesBooksRespository {

    private final SeriesBookMapper seriesBookMapper;
    private final BookRepository bookRepository;
    private final SeriesRepository seriesRepository;

    public SeriesBooksRespositoryImpl(
            SeriesBookMapper seriesBookMapper,
            BookRepository bookRepository,
            SeriesRepository seriesRepository
    ) {
        this.seriesBookMapper = seriesBookMapper;
        this.bookRepository = bookRepository;
        this.seriesRepository = seriesRepository;
    }

    @Override
    public void add(Long bookId, Long seriesId, Long readListId, Long seriesOrder) {
        seriesBookMapper.add(bookId, seriesId, readListId, seriesOrder);
    }

    @Override
    public void add(SeriesBookRelation seriesBookRelation) {
        seriesBookMapper.add(
                seriesBookRelation.book().getBookId(),
                seriesBookRelation.series().getSeriesId(),
                seriesBookRelation.book().getReadListId(),
                seriesBookRelation.order()
        );
    }

    @Override
    public void deleteBySeries(Long seriesId) {
        seriesBookMapper.deleteBySeries(seriesId);
    }

    @Override
    public List<SeriesBookRelation> getByBookId(Long bookId, Long readListId) {
        List<SeriesBookRelation> seriesBookRelationList = new ArrayList<>();
        List<Long> seriesIdList = seriesBookMapper.getSeriesIdsByBookId(bookId, readListId);
        BookImpl book = bookRepository.getOne(readListId, bookId);
        for (Long seriesId : seriesIdList) {
            Series series = seriesRepository.getOne(readListId, seriesId);
            Long order = seriesBookMapper.getOrder(bookId, seriesId, readListId);
            seriesBookRelationList.add(new SeriesBookRelation(book, series, order));
        }
        return seriesBookRelationList;
    }

    @Override
    public List<SeriesBookRelation> getBySeriesId(Long seriesId) throws EntityNotFoundException {
        Optional<Series> optionalSeries = seriesRepository.findById(seriesId);
        optionalSeries.orElseThrow(EntityNotFoundException::new);


//        List<Long> bookIdList = seriesBookMapper.getBookIdsBySeriesId(seriesId);
//        bookIdList.forEach(bookId -> {
//            Optional<Book> optionalBook = bookRepository.getOne(bookId);
//            if (optionalBook.isEmpty()) {
//                throw new RuntimeException();
//            }
//            Long order = seriesBookMapper.getOrderByIdOnly(optionalBook.get().getBookId(), optionalSeries.get().getSeriesId());
//            seriesBookRelationList.add(new SeriesBookRelation(optionalBook.get(), optionalSeries.get(), order));
//        });
        return IntStream.range(0, optionalSeries.get().itemsList().size())
                .filter(ind -> optionalSeries.get().itemsList().get(ind) instanceof BookImpl)
                .mapToObj(ind -> new SeriesBookRelation(
                        (BookImpl) optionalSeries.get().itemsList().get(ind),
                        optionalSeries.get(),
                        (long) ind
                ))
                .collect(Collectors.toCollection(ArrayList::new));
    }

//    @Override
//    public List<SeriesBookRelation> getBySeries(Series series) throws EntityNotFoundException, EmptyMandatoryParameterException {
//        List<SeriesBookRelation> seriesBookRelationList = new ArrayList<>();
//        List<SeriesBookRelationDTO> seriesBookRelationDTOList;
//        try {
//            seriesBookRelationDTOList = MonitoringService.gatherExecutionTime(
//                    () -> seriesBookMapper.findBySeries(series),
//                    MetricType.SERIES_BOOK_MAPPER__FIND_BY_SERIES__EXECUTION_TIME
//            );
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        for (var item : seriesBookRelationDTOList) {
//            Book.Builder builder = new Book.Builder()
//                    .bookId(item.bookId())
//                    .readListId(item.bookListId())
//                    .title(item.title())
//                    .bookStatus(item.bookStatus())
//                    .insertDate(item.insertDate())
//                    .lastUpdateDate(item.lastUpdateDate())
//                    .lastChapter(item.lastChapter())
//                    .bookType(item.bookType());
//            seriesBookRelationList.add(new SeriesBookRelation(
//                    builder.build(),
//                    series,
//                    item.order())
//            );
//        }
//
//        return seriesBookRelationList;
//
//    }

    @Override
    public void update(SeriesBookRelation seriesBookRelation) {
        seriesBookMapper.update(
                seriesBookRelation.book().getBookId(),
                seriesBookRelation.series().getSeriesId(),
                seriesBookRelation.series().getSeriesListId(),
                seriesBookRelation.order()
        );
    }

    @Override
    public void save(List<SeriesBookRelation> seriesBookRelationList) {
        for (var seriesBookRelation : seriesBookRelationList) {
            SeriesBookRelation tmp = new SeriesBookRelation(
                    seriesBookRelation.book(),
                    seriesBookRelation.series(),
                    -seriesBookRelation.order()
            );
            seriesBookMapper.save(tmp);
            seriesBookMapper.merge(seriesBookRelation);
        }
    }

    @Override
    public void delete(Long bookId, Long seriesId, Long readListId) {
        seriesBookMapper.delete(bookId, seriesId, readListId);
    }

    @Override
    public Optional<SeriesBookRelation> getByIds(Long seriesId, Long bookId) throws EntityNotFoundException {
        Optional<Series> optionalSeries = seriesRepository.findById(seriesId);
        optionalSeries.orElseThrow(EntityNotFoundException::new);

        //
//        List<Long> bookIdList = seriesBookMapper.getBookIdsBySeriesId(seriesId);
//
//        Optional<Long> foundBookId = bookIdList.stream()
//                .filter(item -> item.equals(bookId))
//                .findFirst();

         return IntStream.range(
                         0,
                         optionalSeries.get().itemsList().size()
                 )
                 .filter(ind -> optionalSeries.get().itemsList().get(ind) instanceof BookImpl book &&
                         book.getBookId().equals(bookId))
                 .mapToObj(ind -> new SeriesBookRelation(
                         (BookImpl) optionalSeries.get().itemsList().get(ind),
                         optionalSeries.get(),
                         (long) ind
                 ))
                 .findFirst();

//        if (foundBookId.isEmpty()) {
//            throw new EntityNotFoundException();
//        } else {
//            Optional<Book> optionalBook = bookRepository.getOne(bookId);
//            if (optionalBook.isEmpty()) {
//                throw new AssertionError();
//            }
//            Long order = seriesBookMapper.getOrderByIdOnly(optionalBook.get().getBookId(), optionalSeries.get().getSeriesId());
//
//            return Optional.of(new SeriesBookRelation(optionalBook.get(), optionalSeries.get(), order));
//        }
    }
}

package ru.rerumu.lists.repository.impl;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.mappers.SeriesBookMapper;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.repository.BookRepository;
import ru.rerumu.lists.repository.SeriesBooksRespository;
import ru.rerumu.lists.repository.SeriesRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
                seriesBookRelation.getBook().getBookId(),
                seriesBookRelation.getSeries().getSeriesId(),
                seriesBookRelation.getBook().getReadListId(),
                seriesBookRelation.getOrder()
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
        Book book = bookRepository.getOne(readListId, bookId);
        for (Long seriesId : seriesIdList) {
            Series series = seriesRepository.getOne(readListId, seriesId);
            Long order = seriesBookMapper.getOrder(bookId, seriesId, readListId);
            seriesBookRelationList.add(new SeriesBookRelation(book, series, order));
        }
        return seriesBookRelationList;
    }

    @Override
    public List<SeriesBookRelation> getBySeriesId(Long seriesId) throws EntityNotFoundException {
        List<SeriesBookRelation> seriesBookRelationList = new ArrayList<>();
        Optional<Series> optionalSeries = seriesRepository.getOne(seriesId);
        if (optionalSeries.isEmpty()) {
            throw new EntityNotFoundException();
        }
        List<Long> bookIdList = seriesBookMapper.getBookIdsBySeriesId(seriesId);
        bookIdList.forEach(bookId -> {
            Optional<Book> optionalBook = bookRepository.getOne(bookId);
            if (optionalBook.isEmpty()) {
                throw new RuntimeException();
            }
            Long order = seriesBookMapper.getOrderByIdOnly(optionalBook.get().getBookId(), optionalSeries.get().getSeriesId());
            seriesBookRelationList.add(new SeriesBookRelation(optionalBook.get(), optionalSeries.get(), order));
        });
        return seriesBookRelationList;
    }

    @Override
    public void update(SeriesBookRelation seriesBookRelation) {
        seriesBookMapper.update(
                seriesBookRelation.getBook().getBookId(),
                seriesBookRelation.getSeries().getSeriesId(),
                seriesBookRelation.getSeries().getSeriesListId(),
                seriesBookRelation.getOrder()
        );
    }

    @Override
    public void delete(Long bookId, Long seriesId, Long readListId) {
        seriesBookMapper.delete(bookId, seriesId, readListId);
    }

    @Override
    public Optional<SeriesBookRelation> getByIds(Long seriesId, Long bookId) throws EntityNotFoundException {
        Optional<Series> optionalSeries = seriesRepository.getOne(seriesId);
        if (optionalSeries.isEmpty()) {
            throw new EntityNotFoundException();
        }
        List<Long> bookIdList = seriesBookMapper.getBookIdsBySeriesId(seriesId);

        Optional<Long> foundBookId = bookIdList.stream()
                .filter(item -> item.equals(bookId))
                .findFirst();

        if (foundBookId.isEmpty()){
            throw new EntityNotFoundException();
        } else {
            Optional<Book> optionalBook = bookRepository.getOne(bookId);
            if (optionalBook.isEmpty()) {
                throw new AssertionError();
            }
            Long order = seriesBookMapper.getOrderByIdOnly(optionalBook.get().getBookId(), optionalSeries.get().getSeriesId());

            return Optional.of( new SeriesBookRelation(optionalBook.get(), optionalSeries.get(), order));
        }
    }
}

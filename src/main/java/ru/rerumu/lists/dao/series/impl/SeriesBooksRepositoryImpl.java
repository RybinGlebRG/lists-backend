package ru.rerumu.lists.dao.series.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.dao.series.SeriesBooksRepository;
import ru.rerumu.lists.dao.series.mapper.SeriesBookMapper;
import ru.rerumu.lists.domain.series.SeriesBookRelation;

import java.util.List;
import java.util.Optional;

@Component
public class SeriesBooksRepositoryImpl implements SeriesBooksRepository {

    private final SeriesBookMapper seriesBookMapper;

    @Autowired
    public SeriesBooksRepositoryImpl(
            SeriesBookMapper seriesBookMapper
    ) {
        this.seriesBookMapper = seriesBookMapper;
    }

    @Override
    @NonNull
    public SeriesBookRelation create(@NonNull Long bookId, @NonNull Long seriesId, @NonNull Long userId) {
        SeriesBookRelation seriesBookRelation = new SeriesBookRelation(bookId, seriesId, userId);
        seriesBookMapper.create(seriesBookRelation);

        return seriesBookRelation;
    }

    // TODO: Account for userId
    @Override
    public List<SeriesBookRelation> getByBookId(Long bookId, Long readListId, Long userId) {
        throw new NotImplementedException();
    }

    // TODO: fix null
    @Override
    public List<SeriesBookRelation> getBySeriesId(Long seriesId) throws EntityNotFoundException {
        throw new NotImplementedException();
    }

    // TODO: fix null
    @Override
    public void update(SeriesBookRelation seriesBookRelation) {
        throw new NotImplementedException();
    }

    @Override
    public void save(List<SeriesBookRelation> seriesBookRelationList) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(Long bookId, Long seriesId, Long readListId) {
        seriesBookMapper.delete(bookId, seriesId, readListId);
    }

    // TODO: fix null
    @Override
    public Optional<SeriesBookRelation> getByIds(Long seriesId, Long bookId) throws EntityNotFoundException {
        throw new NotImplementedException();
    }

    @Override
    public List<SeriesBookRelationDto> getAllByUserId(@NonNull Long userId) {
        return seriesBookMapper.getByUserId(userId);
    }
}

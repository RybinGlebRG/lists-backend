package ru.rerumu.lists.dao.series;

import lombok.NonNull;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.domain.series.SeriesBookRelation;
import ru.rerumu.lists.dao.series.impl.SeriesBookRelationDto;
import ru.rerumu.lists.domain.series.SeriesItemRelation;

import java.util.List;
import java.util.Optional;

public interface SeriesBooksRepository {

//    @Deprecated
//    void add(Long bookId, Long seriesId, Long readListId, Long seriesOrder);

    @NonNull
    SeriesBookRelation create(@NonNull Long bookId, @NonNull Long seriesId, @NonNull Long userId);

//    void deleteBySeries(Long seriesId);

    List<SeriesBookRelation> getByBookId(Long bookId, Long readListId, Long userId);
    List<SeriesBookRelation> getBySeriesId(Long seriesId) throws EntityNotFoundException;

//    List<SeriesBookRelation> getBySeries(Series series) throws EntityNotFoundException, EmptyMandatoryParameterException;

    void update(SeriesBookRelation seriesBookRelation);

    void save(List<SeriesBookRelation> seriesBookRelationList);
    void delete(Long bookId, Long seriesId, Long readListId);

    Optional<SeriesBookRelation> getByIds(Long seriesId, Long bookId) throws EntityNotFoundException;

    List<SeriesBookRelationDto> getAllByUserId(@NonNull Long userId);

    @NonNull
    List<SeriesItemRelation> fromDTO(@NonNull List<SeriesItemRelationDTO> seriesItemRelationDTOList);
}

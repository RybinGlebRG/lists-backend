package ru.rerumu.lists.dao.series.impl;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.dao.series.SeriesBooksRepository;
import ru.rerumu.lists.dao.series.SeriesItemRelationDTO;
import ru.rerumu.lists.dao.series.SeriesMyBatisEntity;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.dao.series.mapper.SeriesBookMapper;
import ru.rerumu.lists.dao.series.mapper.SeriesMapper;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.dao.base.EntityState;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesBookRelation;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.domain.series.SeriesItemRelation;
import ru.rerumu.lists.domain.user.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SeriesRepositoryImpl implements SeriesRepository{

    private final SeriesMapper seriesMapper;
    private final SeriesBookMapper seriesBookMapper;
    private final SeriesFactory seriesFactory;
    private final SeriesBooksRepository seriesBooksRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public SeriesRepositoryImpl(
            SeriesMapper seriesMapper,
            SeriesBookMapper seriesBookMapper,
            SeriesFactory seriesFactory,
            SeriesBooksRepository seriesBooksRepository,
            UsersRepository usersRepository
    ) {
        this.seriesMapper = seriesMapper;
        this.seriesBookMapper = seriesBookMapper;
        this.seriesFactory = seriesFactory;
        this.seriesBooksRepository = seriesBooksRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public Long getNextId() {
        return seriesMapper.getNextId();
    }

    @Override
    @NonNull
    public List<Series> findByUser(@NonNull User user) {
        List<SeriesMyBatisEntity> seriesMyBatisEntities = seriesMapper.findByUser(user);

        List<Series> seriesList = new ArrayList<>();

        for (SeriesMyBatisEntity seriesMyBatisEntity: seriesMyBatisEntities) {
            seriesList.add(fromMyBatisEntity(seriesMyBatisEntity, user));
        }

        return seriesList;
    }

    @Override
    @NonNull
    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    public Series findById(@NonNull Long seriesId, @NonNull User user) {
        SeriesMyBatisEntity seriesMyBatisEntity = seriesMapper.findById(seriesId, user.getId());
        if (seriesMyBatisEntity == null) {
            throw new EntityNotFoundException();
        }

        Series series = fromMyBatisEntity(seriesMyBatisEntity, user);

        return new SeriesPersistenceProxy(series, EntityState.PERSISTED);
    }

    @Override
    public @NonNull Series create(@NonNull String title, @NonNull User user) {
        Long id = getNextId();

        Series series = seriesFactory.buildSeries(id, title, user);

        seriesMapper.create(SeriesMyBatisEntity.fromDomain(series));

        return new SeriesPersistenceProxy(series, EntityState.PERSISTED);
    }

    @Override
    public Series attach(@NonNull Series series) {
        return new SeriesPersistenceProxy(series, EntityState.PERSISTED);
    }

    @Override
    public Series fromMyBatisEntity(SeriesMyBatisEntity seriesMyBatisEntity, User user) {

        List<SeriesItemRelationDTO> seriesItemRelationDTOList = seriesMyBatisEntity.getSeriesBookRelationDtoList().stream()
                .map(item -> (SeriesItemRelationDTO)item)
                .sorted(Comparator.comparingLong(SeriesItemRelationDTO::getOrder))
                .collect(Collectors.toCollection(ArrayList::new));

        List<SeriesItemRelation> seriesItemRelations = seriesBooksRepository.fromDTO(seriesItemRelationDTOList);

        return seriesFactory.buildSeries(
                seriesMyBatisEntity.getSeriesId(),
                seriesMyBatisEntity.getTitle(),
                user,
                seriesItemRelations
        );
    }

    @Override
    public void delete(long seriesId) {
        List<SeriesBookRelation> seriesBookRelationList = seriesBooksRepository.getBySeriesId(seriesId);
        if (!seriesBookRelationList.isEmpty()) {
            throw new ServerException("EntityHasChildrenException");
        }

        seriesMapper.delete(seriesId);
    }

    @Override
    public List<Series> findByBook(@NonNull Long bookId, @NonNull Long userId) {
        List<SeriesMyBatisEntity> seriesMyBatisEntities = seriesMapper.findByBook(bookId, userId);

        User user = usersRepository.findById(userId);

        List<Series> seriesList = new ArrayList<>();

        for (SeriesMyBatisEntity seriesMyBatisEntity: seriesMyBatisEntities) {
            Series series = fromMyBatisEntity(seriesMyBatisEntity, user);
            series = new SeriesPersistenceProxy(series, EntityState.PERSISTED);
            seriesList.add(series);
        }

        return seriesList;
    }

    @Override
    public List<Series> findByIds(@NonNull List<Long> seriesIds, @NonNull Long userId) {
        List<SeriesMyBatisEntity> seriesMyBatisEntities = seriesMapper.findByIds(seriesIds, userId);

        User user = usersRepository.findById(userId);

        List<Series> seriesList = new ArrayList<>();

        for (SeriesMyBatisEntity seriesMyBatisEntity: seriesMyBatisEntities) {
            Series series = fromMyBatisEntity(seriesMyBatisEntity, user);
            series = new SeriesPersistenceProxy(series, EntityState.PERSISTED);
            seriesList.add(series);
        }

        return seriesList;
    }

    @Override
    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    public void save(@NonNull Series series) {

        SeriesPersistenceProxy seriesPersistenceProxy = (SeriesPersistenceProxy) series;

        switch (seriesPersistenceProxy.getEntityState()) {
            case NEW -> {
                create(SeriesMyBatisEntity.fromDomain(series));
                seriesPersistenceProxy.onSave();
            }
            case DIRTY -> {
                update(
                    SeriesMyBatisEntity.fromDomain(seriesPersistenceProxy.getPersistedCopy()),
                    SeriesMyBatisEntity.fromDomain(series)
                );
                seriesPersistenceProxy.onSave();
            }
            case PERSISTED -> log.warn("Entity is not altered");
            default -> throw new ServerException("Incorrect entity state");
        }
    }

    private void create(SeriesMyBatisEntity myBatisEntity) {
        seriesMapper.create(myBatisEntity);
    }

    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    private void update(SeriesMyBatisEntity originalEntity, SeriesMyBatisEntity currentEntity) {
        seriesMapper.update(currentEntity);

        List<SeriesBookRelationDto> relationsToRemove = originalEntity.getSeriesBookRelationDtoList().stream()
                .filter( item -> !currentEntity.getSeriesBookRelationDtoList().contains(item))
                .collect(Collectors.toCollection(ArrayList::new));
        log.trace("relationsToRemove: {}", relationsToRemove);

        // Remove deleted relations
        for (SeriesBookRelationDto seriesBookRelationDto: relationsToRemove) {
            seriesBookMapper.delete(seriesBookRelationDto);
        }

        // Add or update relations
        for (SeriesBookRelationDto seriesBookRelationDto: currentEntity.getSeriesBookRelationDtoList()) {
            seriesBookMapper.merge(seriesBookRelationDto);
        }

    }
}

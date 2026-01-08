package ru.rerumu.lists.dao.series.impl;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.dao.series.SeriesMyBatisEntity;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.dao.series.mapper.SeriesBookMapper;
import ru.rerumu.lists.dao.series.mapper.SeriesMapper;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesBookRelationDto;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.domain.series.impl.SeriesImpl;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SeriesRepositoryImpl implements SeriesRepository{

    private final SeriesMapper seriesMapper;
    private final SeriesBookMapper seriesBookMapper;
    private final ReadingRecordsRepository readingRecordsRepository;
    private final SeriesFactory seriesFactory;
    private final UserFactory userFactory;

    @Autowired
    public SeriesRepositoryImpl(
            SeriesMapper seriesMapper,
            SeriesBookMapper seriesBookMapper,
            ReadingRecordsRepository readingRecordsRepository,
            SeriesFactory seriesFactory,
            UserFactory userFactory
    ) {
        this.seriesMapper = seriesMapper;
        this.seriesBookMapper = seriesBookMapper;
        this.readingRecordsRepository = readingRecordsRepository;
        this.seriesFactory = seriesFactory;
        this.userFactory = userFactory;
    }

//    @Override
//    public List<SeriesDTO> getAll(Long seriesListId) {
//
//        try {
//            List<SeriesDTO> res = seriesMapper.getAll(seriesListId);
//
//            List<Long> bookIds = res.stream()
//                    .flatMap(seriesDTO -> seriesDTO.seriesItemOrderDTOList.stream())
//                    .filter(seriesItemOrderDTO -> seriesItemOrderDTO.itemDTO instanceof BookMyBatisEntity)
//                    .map(seriesItemOrderDTO -> ((BookMyBatisEntity)seriesItemOrderDTO.itemDTO).getBookId())
//                    .collect(Collectors.toCollection(ArrayList::new));
//
//            List<ReadingRecord> readingRecords = readingRecordsRepository.findByBookIds(bookIds);
//
//            Map<Long, List<ReadingRecord>> bookId2ReadingRecordMap = readingRecords.stream()
//                    .collect(Collectors.groupingBy(
//                            ReadingRecord::getBookId,
//                            HashMap::new,
//                            Collectors.toCollection(ArrayList::new)
//                    ));
//
//            List<BookMyBatisEntity> bookDTOList = res.stream()
//                    .flatMap(seriesDTO -> seriesDTO.seriesItemOrderDTOList.stream())
//                    .filter(seriesItemOrderDTO -> seriesItemOrderDTO.itemDTO instanceof BookMyBatisEntity)
//                    .map(seriesItemOrderDTO -> (BookMyBatisEntity)seriesItemOrderDTO.itemDTO)
//                    .collect(Collectors.toCollection(ArrayList::new));
//
//            for(BookMyBatisEntity bookMyBatisEntity: bookDTOList){
//                List<ReadingRecord> records = bookId2ReadingRecordMap.get(bookMyBatisEntity.getBookId());
//
//                if (records == null){
//                    records = new ArrayList<>();
//                }
//
//                bookMyBatisEntity.setReadingRecords(
//                        records.stream()
//                                .map(ReadingRecordMyBatisEntity::fromDomain)
//                                .collect(Collectors.toCollection(ArrayList::new))
//                );
//            }
//
//            List<SeriesDTO> resList = new ArrayList<>(res);
//            return resList;
//        } catch (Exception e){
//            throw new RuntimeException(e);
//        }
//
//    }

//    @Override
//    public int getBookCount(Long readListId, Long seriesId) {
//        return seriesMapper.getBookCount(readListId, seriesId);
//    }

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
            seriesList.add(seriesFactory.fromMyBatisEntity(seriesMyBatisEntity, user));
        }

        return seriesList;
    }

    @Override
    @NonNull
    public Series findById(@NonNull Long seriesId, @NonNull User user) {
        SeriesMyBatisEntity seriesMyBatisEntity = seriesMapper.findById(seriesId, user.userId());
        if (seriesMyBatisEntity == null) {
            throw new EntityNotFoundException();
        }

        return seriesFactory.fromMyBatisEntity(seriesMyBatisEntity, user);
    }

//    @Override
//    public void add(SeriesDTO series) {
//        seriesMapper.add(
//                series.seriesListId,
//                series.seriesId,
//                series.title
//        );
//    }

    @Override
    public void delete(long seriesId) {
        seriesMapper.delete(seriesId);
    }

    @Override
    public List<Series> findByBook(@NonNull Long bookId, @NonNull Long userId) {
        List<SeriesMyBatisEntity> seriesMyBatisEntities = seriesMapper.findByBook(bookId, userId);

        User user = userFactory.findById(userId);

        List<Series> seriesList = new ArrayList<>();

        for (SeriesMyBatisEntity seriesMyBatisEntity: seriesMyBatisEntities) {
            seriesList.add(seriesFactory.fromMyBatisEntity(seriesMyBatisEntity, user));
        }

        return seriesList;
    }

    @Override
    public List<Series> findByIds(@NonNull List<Long> seriesIds, @NonNull Long userId) {
        List<SeriesMyBatisEntity> seriesMyBatisEntities = seriesMapper.findByIds(seriesIds, userId);

        User user = userFactory.findById(userId);

        List<Series> seriesList = new ArrayList<>();

        for (SeriesMyBatisEntity seriesMyBatisEntity: seriesMyBatisEntities) {
            seriesList.add(seriesFactory.fromMyBatisEntity(seriesMyBatisEntity, user));
        }

        return seriesList;
    }

    @Override
    public void save(@NonNull Series series) {

        SeriesImpl seriesImpl = (SeriesImpl) series;

        switch (seriesImpl.getEntityState()) {
            case NEW -> create(SeriesMyBatisEntity.fromDomain(series));
            case DIRTY -> update(
                    SeriesMyBatisEntity.fromDomain(seriesImpl.getPersistedCopy()),
                    SeriesMyBatisEntity.fromDomain(series)
            );
            case PERSISTED -> log.warn("Entity is not altered");
            default -> throw new ServerException("Incorrect entity state");
        }

        seriesImpl.setEntityState(EntityState.PERSISTED);
    }

    private void create(SeriesMyBatisEntity myBatisEntity) {
        seriesMapper.create(myBatisEntity);
    }

    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    public void update(SeriesMyBatisEntity originalEntity, SeriesMyBatisEntity currentEntity) {
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

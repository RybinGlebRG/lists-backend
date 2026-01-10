package ru.rerumu.lists.dao.readingrecord.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordMyBatisEntity;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.dao.readingrecord.mapper.ReadingRecordMapper;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.readingrecord.ReadingRecord;
import ru.rerumu.lists.domain.readingrecord.impl.ReadingRecordFactory;
import ru.rerumu.lists.domain.readingrecordstatus.ReadingRecordStatuses;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class ReadingRecordRepositoryImpl implements ReadingRecordsRepository {

    private final ReadingRecordMapper mapper;
    private final ReadingRecordFactory readingRecordFactory;

    @Autowired
    public ReadingRecordRepositoryImpl(
            ReadingRecordMapper mapper,
            ReadingRecordFactory readingRecordFactory
    ) {
        this.mapper = mapper;
        this.readingRecordFactory = readingRecordFactory;
    }

    @Override
    public List<ReadingRecord> findByBookId(Long bookId) {
        List<ReadingRecordMyBatisEntity> readingRecordMyBatisEntities = mapper.findByBookId(bookId);

        List<ReadingRecord> readingRecords = readingRecordMyBatisEntities.stream()

                // Map from DTO
                .map(readingRecordFactory::fromDTO)

                // Init proxy
                .map(readingRecord -> {
                    ReadingRecordPersistenceProxy readingRecordPersistenceProxy = new ReadingRecordPersistenceProxy(
                            readingRecord,
                            EntityState.PERSISTED
                    );
                    readingRecordPersistenceProxy.initPersistedCopy();
                    return readingRecordPersistenceProxy;
                } )

                .collect(Collectors.toCollection(ArrayList::new));

        return readingRecords;
    }

    @Override
    public List<ReadingRecord> findByBookIds(List<Long> bookIds) {
        List<ReadingRecordMyBatisEntity> readingRecordMyBatisEntities = mapper.findByBookIds(bookIds);

        List<ReadingRecord> readingRecords = readingRecordMyBatisEntities.stream()

                // Map from DTO
                .map(readingRecordFactory::fromDTO)

                // Init proxy
                .map(readingRecord -> {
                    ReadingRecordPersistenceProxy readingRecordPersistenceProxy = new ReadingRecordPersistenceProxy(
                            readingRecord,
                            EntityState.PERSISTED
                    );
                    readingRecordPersistenceProxy.initPersistedCopy();
                    return readingRecordPersistenceProxy;
                } )

                .collect(Collectors.toCollection(ArrayList::new));

        return readingRecords;
    }

    @Override
    public void delete(Long readingRecordId) {
        mapper.delete(readingRecordId);
    }

    @Override
    public void delete(ReadingRecord readingRecord) {
        mapper.delete(readingRecord.getId());

        ReadingRecordPersistenceProxy readingRecordPersistenceProxy = (ReadingRecordPersistenceProxy) readingRecord;
        readingRecordPersistenceProxy.setEntityState(EntityState.DELETED);
        readingRecordPersistenceProxy.clearPersistedCopy();
    }

    @Override
    public Long getNextId() {
        return mapper.getNextId();
    }

    @Override
    public ReadingRecord create(
            @NonNull Long bookId,
            @NonNull ReadingRecordStatuses bookStatusRecord,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    ) {
        Long readingRecordId = getNextId();

        ReadingRecord readingRecord = readingRecordFactory.createReadingRecord(
                readingRecordId,
                bookId,
                bookStatusRecord,
                startDate,
                endDate,
                lastChapter
        );

        ReadingRecordMyBatisEntity readingRecordMyBatisEntity = new ReadingRecordMyBatisEntity(
                readingRecord.getId(),
                readingRecord.getBookId(),
                readingRecord.getBookStatus(),
                readingRecord.getStartDate(),
                readingRecord.getEndDate(),
                readingRecord.getIsMigrated(),
                readingRecord.getLastChapter(),
                readingRecord.getUpdateDate()
        );

        mapper.create(readingRecordMyBatisEntity);

        ReadingRecordPersistenceProxy readingRecordPersistenceProxy = new ReadingRecordPersistenceProxy(
                readingRecord,
                EntityState.PERSISTED
        );
        readingRecordPersistenceProxy.initPersistedCopy();

        return readingRecordPersistenceProxy;
    }

    @Override
    public ReadingRecord create(@NonNull Long bookId, @NonNull Long statusId, LocalDateTime startDate, LocalDateTime endDate, Long lastChapter) {
        return null;
    }

    @Override
    public ReadingRecord findById(@NonNull Long id) {
        return null;
    }

    @Override
    public void update(ReadingRecord readingRecord) {
        ReadingRecordMyBatisEntity readingRecordMyBatisEntity = new ReadingRecordMyBatisEntity(
                readingRecord.getId(),
                readingRecord.getBookId(),
                readingRecord.getBookStatus(),
                readingRecord.getStartDate(),
                readingRecord.getEndDate(),
                readingRecord.getIsMigrated(),
                readingRecord.getLastChapter(),
                readingRecord.getUpdateDate()
        );

        mapper.update(readingRecordMyBatisEntity);

        ReadingRecordPersistenceProxy readingRecordPersistenceProxy = (ReadingRecordPersistenceProxy) readingRecord;
        readingRecordPersistenceProxy.setEntityState(EntityState.PERSISTED);
        readingRecordPersistenceProxy.initPersistedCopy();
    }

    @Override
    public ReadingRecord attach(ReadingRecord readingRecord) {
        ReadingRecordPersistenceProxy readingRecordPersistenceProxy = new ReadingRecordPersistenceProxy(
                readingRecord,
                EntityState.PERSISTED
        );
        readingRecordPersistenceProxy.initPersistedCopy();

        return readingRecordPersistenceProxy;
    }
}

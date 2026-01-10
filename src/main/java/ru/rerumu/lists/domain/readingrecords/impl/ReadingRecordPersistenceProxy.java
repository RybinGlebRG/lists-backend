package ru.rerumu.lists.domain.readingrecords.impl;

import lombok.NonNull;
import org.json.JSONObject;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.base.PersistenceProxy;
import ru.rerumu.lists.domain.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.readingrecordstatus.ReadingRecordStatuses;

import java.time.LocalDateTime;

public class ReadingRecordPersistenceProxy extends PersistenceProxy<ReadingRecord> implements ReadingRecord {

    private final ReadingRecord readingRecord;

    public ReadingRecordPersistenceProxy(ReadingRecord readingRecord, EntityState entityState) {
        super(entityState);
        this.readingRecord = readingRecord;
    }

    @Override
    public void initPersistedCopy() {
        persistedCopy = readingRecord.deepCopy();
    }

    @Override
    public void clearPersistedCopy() {
        persistedCopy = null;
    }

    @Override
    public Long getId() {
        return readingRecord.getId();
    }

    @Override
    public Long getBookId() {
        return readingRecord.getBookId();
    }

    @Override
    public LocalDateTime getUpdateDate() {
        return readingRecord.getUpdateDate();
    }

    @Override
    public LocalDateTime getStartDate() {
        return readingRecord.getStartDate();
    }

    @Override
    public LocalDateTime getEndDate() {
        return readingRecord.getEndDate();
    }

    @Override
    public ReadingRecordStatuses getBookStatus() {
        return readingRecord.getBookStatus();
    }

    @Override
    public Long getLastChapter() {
        return readingRecord.getLastChapter();
    }

    @Override
    public Boolean getIsMigrated() {
        return readingRecord.getIsMigrated();
    }

    @Override
    public boolean update(@NonNull ReadingRecordStatuses bookStatusRecord, @NonNull LocalDateTime startDate, LocalDateTime endDate, Long lastChapter) {
        boolean isChanged = readingRecord.update(bookStatusRecord, startDate, endDate, lastChapter);
        if (isChanged) {
            entityState = EntityState.DIRTY;
        }
        return isChanged;
    }

    @Override
    public boolean statusEquals(@NonNull Long statusId) {
        return readingRecord.statusEquals(statusId);
    }

    @Override
    public JSONObject toJSONObject() {
        return readingRecord.toJSONObject();
    }

    @Override
    public ReadingRecord deepCopy() {
        return readingRecord.deepCopy();
    }
}

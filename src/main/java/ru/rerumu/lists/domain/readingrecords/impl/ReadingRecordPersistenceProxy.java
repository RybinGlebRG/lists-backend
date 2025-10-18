package ru.rerumu.lists.domain.readingrecords.impl;

import lombok.NonNull;
import org.json.JSONObject;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.base.PersistenceProxy;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;
import ru.rerumu.lists.domain.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.readingrecords.ReadingRecordDTO;

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
    public void save() {
        readingRecord.save();
    }

    @Override
    public void delete() {
        readingRecord.delete();
    }

    @Override
    public void update(@NonNull BookStatusRecord bookStatusRecord, @NonNull LocalDateTime startDate, LocalDateTime endDate, Long lastChapter) {
        readingRecord.update(bookStatusRecord, startDate, endDate, lastChapter);
        entityState = EntityState.DIRTY;
    }

    @Override
    public boolean statusEquals(@NonNull Long statusId) {
        return readingRecord.statusEquals(statusId);
    }

    @Override
    public ReadingRecordDTO toDTO() {
        return readingRecord.toDTO();
    }

    @Override
    public JSONObject toJSONObject() {
        return readingRecord.toJSONObject();
    }

    @Override
    public int compareTo(@NonNull ReadingRecord o) {
        return readingRecord.compareTo(o);
    }

    @Override
    public ReadingRecord deepCopy() {
        return readingRecord.deepCopy();
    }
}

package ru.rerumu.lists.services.protection_proxies;

import ru.rerumu.lists.exception.UserPermissionException;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;
import ru.rerumu.lists.services.ReadListService;
import ru.rerumu.lists.services.ReadingRecordService;
import ru.rerumu.lists.views.ReadingRecordAddView;
import ru.rerumu.lists.views.ReadingRecordUpdateView;

import java.util.List;
import java.util.Optional;

public class ReadingRecordProtectionProxy implements ReadingRecordService {

    private final ReadingRecordService readingRecordService;
    private final User authUser;
    private final ReadListService readListService;

    public ReadingRecordProtectionProxy(ReadingRecordService readingRecordService, User authUser, ReadListService readListService) {
        this.readingRecordService = readingRecordService;
        this.authUser = authUser;
        this.readListService = readListService;
    }

    @Override
    public ReadingRecord addRecord(Long bookId, ReadingRecordAddView readingRecordAddView) {
        User user = readListService.getBookUser(bookId).orElseThrow();
        if (!user.equals(authUser)){
            throw new UserPermissionException();
        }

        return readingRecordService.addRecord(bookId, readingRecordAddView);
    }

    @Override
    public ReadingRecord addRecord(Long bookId, ReadingRecord readingRecord) {
        User user = readListService.getBookUser(bookId).orElseThrow();
        if (!user.equals(authUser)){
            throw new UserPermissionException();
        }

        return readingRecordService.addRecord(bookId, readingRecord);
    }

    @Override
    public ReadingRecord updateRecord(Long recordId, ReadingRecordUpdateView readingRecordUpdateView) {
        ReadingRecord readingRecord = readingRecordService.getReadingRecord(recordId).orElseThrow();
        User user = readListService.getBookUser(readingRecord.bookId()).orElseThrow();
        if (!user.equals(authUser)){
            throw new UserPermissionException();
        }

        return readingRecordService.updateRecord(recordId, readingRecordUpdateView);
    }

    @Override
    public void deleteRecord(Long recordId) {
        ReadingRecord readingRecord = readingRecordService.getReadingRecord(recordId).orElseThrow();
        User user = readListService.getBookUser(readingRecord.bookId()).orElseThrow();
        if (!user.equals(authUser)){
            throw new UserPermissionException();
        }

        readingRecordService.deleteRecord(recordId);
    }

    @Override
    public List<ReadingRecord> getReadingRecords(Long bookId) {
        User user = readListService.getBookUser(bookId).orElseThrow();
        if (!user.equals(authUser)){
            throw new UserPermissionException();
        }

        return readingRecordService.getReadingRecords(bookId);
    }

    @Override
    public Optional<ReadingRecord> getReadingRecord(Long readingRecordId) {
        ReadingRecord readingRecord = readingRecordService.getReadingRecord(readingRecordId).orElseThrow();
        User user = readListService.getBookUser(readingRecord.bookId()).orElseThrow();
        if (!user.equals(authUser)){
            throw new UserPermissionException();
        }
        return Optional.of(readingRecord);
    }
}
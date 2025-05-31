package ru.rerumu.lists.services.protectionproxies;

import ru.rerumu.lists.crosscut.exception.UserPermissionException;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.book.readingrecords.ReadingRecord;
import ru.rerumu.lists.model.book.readingrecords.impl.ReadingRecordImpl;
import ru.rerumu.lists.services.book.impl.ReadListService;
import ru.rerumu.lists.services.book.readingrecord.ReadingRecordService;
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
    public ReadingRecordImpl addRecord(Long bookId, ReadingRecordAddView readingRecordAddView) {
        User user = readListService.getBookUser(bookId).orElseThrow();
        if (!user.equals(authUser)){
            throw new UserPermissionException();
        }

        return readingRecordService.addRecord(bookId, readingRecordAddView);
    }

//    @Override
//    public ReadingRecordImpl addRecord(ReadingRecordImpl readingRecord) {
//        return readingRecordService.addRecord(readingRecord);
//    }

    @Override
    public Long getNextId() {
        return readingRecordService.getNextId();
    }

//    @Override
//    public ReadingRecordImpl addRecord(Long bookId, ReadingRecordImpl readingRecord) {
//        User user = readListService.getBookUser(bookId).orElseThrow();
//        if (!user.equals(authUser)){
//            throw new UserPermissionException();
//        }
//
//        return readingRecordService.addRecord(bookId, readingRecord);
//    }

    @Override
    public ReadingRecord updateRecord(Long recordId, ReadingRecordUpdateView readingRecordUpdateView) {
        ReadingRecord readingRecord = readingRecordService.getReadingRecord(recordId).orElseThrow();
        User user = readListService.getBookUser(readingRecord.getBookId()).orElseThrow();
        if (!user.equals(authUser)){
            throw new UserPermissionException();
        }

        return readingRecordService.updateRecord(recordId, readingRecordUpdateView);
    }

    @Override
    public ReadingRecordImpl updateRecord(ReadingRecordImpl readingRecord) {
        User user = readListService.getBookUser(readingRecord.getBookId()).orElseThrow();
        if (!user.equals(authUser)){
            throw new UserPermissionException();
        }

        return readingRecordService.updateRecord(readingRecord);
    }

    @Override
    public void deleteRecord(Long recordId) {
        ReadingRecord readingRecord = readingRecordService.getReadingRecord(recordId).orElseThrow();
        User user = readListService.getBookUser(readingRecord.getBookId()).orElseThrow();
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
        User user = readListService.getBookUser(readingRecord.getBookId()).orElseThrow();
        if (!user.equals(authUser)){
            throw new UserPermissionException();
        }
        return Optional.of(readingRecord);
    }
}

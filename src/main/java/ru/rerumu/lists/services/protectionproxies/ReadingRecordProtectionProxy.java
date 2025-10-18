package ru.rerumu.lists.services.protectionproxies;

import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.book.impl.ReadListService;
import ru.rerumu.lists.services.book.readingrecord.ReadingRecordService;

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
    public Long getNextId() {
        return readingRecordService.getNextId();
    }
}

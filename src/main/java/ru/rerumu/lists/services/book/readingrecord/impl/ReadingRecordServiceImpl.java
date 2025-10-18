package ru.rerumu.lists.services.book.readingrecord.impl;

import lombok.extern.slf4j.Slf4j;
import ru.rerumu.lists.dao.book.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.domain.readingrecords.impl.ReadingRecordFactory;
import ru.rerumu.lists.services.book.readingrecord.ReadingRecordService;
import ru.rerumu.lists.services.book.status.BookStatusesService;

@Slf4j
public class ReadingRecordServiceImpl implements ReadingRecordService {

    private final ReadingRecordsRepository crudRepository;

    private final BookStatusesService bookStatusesService;

    private final ReadingRecordFactory readingRecordFactory;

    public ReadingRecordServiceImpl(ReadingRecordsRepository crudRepository, BookStatusesService bookStatusesService, ReadingRecordFactory readingRecordFactory) {
        this.crudRepository = crudRepository;
        this.bookStatusesService = bookStatusesService;
        this.readingRecordFactory = readingRecordFactory;
    }

    @Override
    public Long getNextId() {
        return crudRepository.getNextId();
    }
}

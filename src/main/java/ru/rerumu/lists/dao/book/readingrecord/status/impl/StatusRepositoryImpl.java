package ru.rerumu.lists.dao.book.readingrecord.status.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.book.readingrecord.status.StatusRepository;
import ru.rerumu.lists.dao.book.readingrecord.status.mapper.BookStatusMapper;
import ru.rerumu.lists.model.book.readingrecords.status.BookStatusRecord;

@Component
public class StatusRepositoryImpl implements StatusRepository {

    private final BookStatusMapper statusMapper;

    @Autowired
    public StatusRepositoryImpl(BookStatusMapper statusMapper) {
        this.statusMapper = statusMapper;
    }

    @Override
    @NonNull
    public BookStatusRecord findById(@NonNull Long statusId) {
        BookStatusRecord bookStatusRecord = statusMapper.findById(statusId.intValue());
        if (bookStatusRecord == null) {
            throw new EntityNotFoundException();
        }

        return bookStatusRecord;
    }
}

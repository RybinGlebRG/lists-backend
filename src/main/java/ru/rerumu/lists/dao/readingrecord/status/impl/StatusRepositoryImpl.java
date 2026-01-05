package ru.rerumu.lists.dao.readingrecord.status.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.readingrecord.status.StatusRepository;
import ru.rerumu.lists.dao.readingrecord.status.mapper.BookStatusMapper;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;

// TODO: fix null
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
        BookStatusRecord bookStatusRecord = statusMapper.findById(statusId.intValue(), null);
        if (bookStatusRecord == null) {
            throw new EntityNotFoundException();
        }

        return bookStatusRecord;
    }
}

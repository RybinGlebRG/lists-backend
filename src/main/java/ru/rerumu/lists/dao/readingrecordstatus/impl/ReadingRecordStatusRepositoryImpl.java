package ru.rerumu.lists.dao.readingrecordstatus.impl;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.readingrecordstatus.ReadingRecordStatusRepository;
import ru.rerumu.lists.domain.readingrecordstatus.ReadingRecordStatuses;


@Component
public class ReadingRecordStatusRepositoryImpl implements ReadingRecordStatusRepository {

    @Override
    @NonNull
    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    public ReadingRecordStatuses findById(@NonNull Long statusId) {
        ReadingRecordStatuses bookStatusRecord = null;

        for (ReadingRecordStatuses item: ReadingRecordStatuses.values()) {
            if (item.getId().equals(statusId)) {
                bookStatusRecord = item;
            }
        }

        if (bookStatusRecord == null) {
            throw new EntityNotFoundException();
        }

        return bookStatusRecord;
    }
}

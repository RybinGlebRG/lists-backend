package ru.rerumu.lists.dao.readingrecordstatus;

import lombok.NonNull;
import ru.rerumu.lists.domain.readingrecordstatus.ReadingRecordStatuses;

public interface ReadingRecordStatusRepository {

    @NonNull
    ReadingRecordStatuses findById(@NonNull Long statusId);
}

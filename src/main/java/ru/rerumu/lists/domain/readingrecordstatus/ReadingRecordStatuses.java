package ru.rerumu.lists.domain.readingrecordstatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReadingRecordStatuses {
    IN_PROGRESS(1L, "In progress"),
    COMPLETED(2L, "Completed"),
    EXPECTING(3L, "Expecting"),
    DROPPED(4L, "Dropped");

    private final Long id;
    private final String name;

}

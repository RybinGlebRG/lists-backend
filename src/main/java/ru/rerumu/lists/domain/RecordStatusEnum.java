package ru.rerumu.lists.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RecordStatusEnum {
    IN_PROGRESS(1L),
    COMPLETED(2L),
    EXPECTING(3L),
    DROPPED(4L);

    private final Long id;
}

package ru.rerumu.lists.model.book.readingrecords;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RecordDTO {

    Long recordId;
    Long bookId;
    Long statusId;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Long lastChapter;

}

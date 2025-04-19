package ru.rerumu.lists.model.book.readingrecords;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class RecordDTO {

    Long recordId;
    Long bookId;
    Long statusId;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Long lastChapter;

}

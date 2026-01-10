package ru.rerumu.lists.dao.readingrecord;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.rerumu.lists.dao.base.MyBatisEntity;
import ru.rerumu.lists.domain.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.readingrecordstatus.ReadingRecordStatuses;

import java.time.LocalDateTime;

/**
 * MyBatis Entity for ReadingRecord
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ReadingRecordMyBatisEntity implements MyBatisEntity {
    
    private Long recordId;
    private Long bookId;
    private ReadingRecordStatuses bookStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isMigrated;
    private Long lastChapter;
    private LocalDateTime updateDate;

    public static ReadingRecordMyBatisEntity fromDomain(ReadingRecord readingRecord) {
        return new ReadingRecordMyBatisEntity(
                readingRecord.getId(),
                readingRecord.getBookId(),
                readingRecord.getBookStatus(),
                readingRecord.getStartDate(),
                readingRecord.getEndDate(),
                readingRecord.getIsMigrated(),
                readingRecord.getLastChapter(),
                readingRecord.getUpdateDate()
        );
    }
}

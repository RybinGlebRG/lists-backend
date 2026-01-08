package ru.rerumu.lists.controller.readingrecord.view.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.rerumu.lists.crosscut.DeepCopyable;
import ru.rerumu.lists.controller.book.view.out.BookStatusView;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Getter
@Builder(toBuilder = true, access = AccessLevel.PRIVATE)
public class ReadingRecordView implements DeepCopyable<ReadingRecordView> {

    private final Long recordId;

    private final Long bookId;

    private final BookStatusView bookStatus;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime endDate;

    private final Boolean isMigrated;

    private final Long lastChapter;

    private final LocalDateTime updateDate;

    public ReadingRecordView(
            Long recordId,
            Long bookId,
            BookStatusView bookStatus,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Boolean isMigrated,
            Long lastChapter,
            LocalDateTime updateDate
    ) {
        this.recordId = recordId;
        this.bookId = bookId;
        this.bookStatus = bookStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isMigrated = isMigrated;
        this.lastChapter = lastChapter;
        this.updateDate = updateDate;
    }


    @Override
    public ReadingRecordView deepCopy() {
        return this.toBuilder()
                .bookStatus(bookStatus.deepCopy())
                .build();
    }
}

package ru.rerumu.lists.controller.readingrecord.view.out;

import lombok.Getter;
import ru.rerumu.lists.controller.book.view.out.BookStatusView;

import java.time.LocalDateTime;

@Getter
public class ReadingRecordView {

    private final Long recordId;
    private final Long bookId;
    private final BookStatusView bookStatus;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Boolean isMigrated;
    private final Long lastChapter;

    public ReadingRecordView(
            Long recordId,
            Long bookId,
            BookStatusView bookStatus,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Boolean isMigrated,
            Long lastChapter
    ) {
        this.recordId = recordId;
        this.bookId = bookId;
        this.bookStatus = bookStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isMigrated = isMigrated;
        this.lastChapter = lastChapter;
    }
}

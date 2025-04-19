package ru.rerumu.lists.controller.book.view.in;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
public class BookUpdateView {
    private final Long readListId;
    private final String title;
    private final Long authorId;
    private final Long seriesId;
    private final Long order;
    private final Integer lastChapter;
    private final LocalDateTime insertDateUTC;
    private final Integer bookTypeId;
    private final String note;
    private final String URL;
    private final List<Long> tagIds;
    private final List<ReadingRecordView> readingRecords;

    @Getter
    public static class ReadingRecordView {
        private final Long readingRecordId;
        private final Integer statusId;
        private final LocalDateTime startDate;
        private final LocalDateTime endDate;
        private final Long lastChapter;

        @JsonCreator
        public ReadingRecordView(
                Long readingRecordId,
                Integer statusId,
                @NonNull LocalDateTime startDate,
                LocalDateTime endDate,
                Long lastChapter
        ) {
            this.readingRecordId = readingRecordId;
            this.statusId = statusId;
            this.startDate = startDate;
            this.endDate = endDate;
            this.lastChapter = lastChapter;
        }
    }

    @JsonCreator
    public BookUpdateView(
            @NonNull Long readListId,
            @NonNull String title,
            Long authorId,
            Long seriesId,
            Long order,
            Integer lastChapter,
            LocalDateTime insertDateUTC,
            Integer bookTypeId,
            String note,
            String URL,
            @NonNull List<Long> tagIds,
            @NonNull List<ReadingRecordView> readingRecords
    ) {
        this.readListId = readListId;
        this.title = title;
        this.authorId = authorId;
        this.seriesId = seriesId;
        this.order = order;
        this.lastChapter = lastChapter;
        this.insertDateUTC = insertDateUTC;
        this.bookTypeId = bookTypeId;
        this.note = note;
        this.URL = URL;
        this.tagIds = tagIds;
        this.readingRecords = readingRecords;
    }
}

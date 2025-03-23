package ru.rerumu.lists.controller.book.view.out;

import lombok.Getter;
import ru.rerumu.lists.controller.readingrecord.view.out.ReadingRecordView;
import ru.rerumu.lists.controller.tag.view.out.TagView;

import java.time.LocalDateTime;
import java.util.List;

public class BookView {

    @Getter
    private final Long bookId;

    @Getter
    private final Long readListId;

    @Getter
    private final String title;

    @Getter
    private final BookStatusView bookStatus;

    @Getter
    private final LocalDateTime insertDate;

    @Getter
    private final LocalDateTime lastUpdateDate;

    @Getter
    private final Integer lastChapter;

    @Getter
    private final String note;

    @Getter
    private final BookType bookType;
    public static class BookType {

        @Getter
        private final Integer typeId;

        @Getter
        private final String typeName;

        public BookType(Integer typeId, String typeName) {
            this.typeId = typeId;
            this.typeName = typeName;
        }
    }

    @Getter
    private final String itemType;

    @Getter
    private final List<BookView> chain;

    @Getter
    private final List<ReadingRecordView> readingRecords;

    @Getter
    private final String URL;

    @Getter
    private final List<TagView> tags;

    public BookView(
            Long bookId,
            Long readListId,
            String title,
            BookStatusView bookStatus,
            LocalDateTime insertDate,
            LocalDateTime lastUpdateDate,
            Integer lastChapter,
            String note,
            BookType bookType,
            String itemType,
            List<BookView> chain,
            List<ReadingRecordView> readingRecords,
            String URL,
            List<TagView> tags
    ) {
        this.bookId = bookId;
        this.readListId = readListId;
        this.title = title;
        this.bookStatus = bookStatus;
        this.insertDate = insertDate;
        this.lastUpdateDate = lastUpdateDate;
        this.lastChapter = lastChapter;
        this.note = note;
        this.bookType = bookType;
        this.itemType = itemType;
        this.chain = chain;
        this.readingRecords = readingRecords;
        this.URL = URL;
        this.tags = tags;
    }
}

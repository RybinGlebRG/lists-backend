package ru.rerumu.lists.controller.book.view.out;

import lombok.Getter;
import ru.rerumu.lists.controller.views.ReadingRecordView;
import ru.rerumu.lists.controller.views.TagView;
import ru.rerumu.lists.model.book.BookDTO;
import ru.rerumu.lists.model.series.item.SeriesItemType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookView {

    @Getter
    private final Long bookId;

    @Getter
    private final Long readListId;

    @Getter
    private final String title;

    @Getter
    private final BookStatusView bookStatus;
    public static class BookStatusView{

        private final Integer statusId;
        private final String statusName;

        public BookStatusView(Integer statusId, String statusName) {
            this.statusId = statusId;
            this.statusName = statusName;
        }
    }

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

        private final Integer typeId;
        private final String typeName;

        public BookType(Integer typeId, String typeName) {
            this.typeId = typeId;
            this.typeName = typeName;
        }
    }

    @Getter
    private final String itemType;

    @Getter
    private final List<BookOrderedView> chain;

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
            List<BookOrderedView> chain,
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

    public BookView(BookDTO bookDTO) {
        bookId = bookDTO.getBookId();
        readListId =  bookDTO.getReadListId();
        title = bookDTO.getTitle();
        bookStatus = new ru.rerumu.lists.controller.book.view.out.BookView.BookStatusView(
                        bookDTO.getBookStatusObj().statusId(),
                        bookDTO.getBookStatusObj().statusName()
                );
        insertDate = bookDTO.getLastInsertLocalDate();
        lastUpdateDate = bookDTO.getLastUpdateDate_V2();
        lastChapter = bookDTO.lastChapter;
        note = bookDTO.getNote();
        bookType = new ru.rerumu.lists.controller.book.view.out.BookView.BookType(
                        bookDTO.getBookTypeObj().id,
                        bookDTO.getBookTypeObj().name
                );
       itemType = SeriesItemType.BOOK.name();

       if (bookDTO.getPreviousBooks() != null) {
           chain = bookDTO.getPreviousBooks().stream()
                   .map(bookOrderedDTO -> new BookOrderedView(
                           new BookView(bookOrderedDTO.getBookDTO()),
                           bookOrderedDTO.getOrder()
                   ))
                   .collect(Collectors.toCollection(ArrayList::new));
       } else {
           chain = new ArrayList<>();
       }

       readingRecords = bookDTO.getReadingRecords().stream()
               .map(readingRecordDTO -> new ReadingRecordView())
               .collect(Collectors.toCollection(ArrayList::new));

       URL = bookDTO.getURL();
       tags = bookDTO.getTags().stream()
               .map(tagDTO -> new TagView())
               .collect(Collectors.toCollection(ArrayList::new));
    }
}

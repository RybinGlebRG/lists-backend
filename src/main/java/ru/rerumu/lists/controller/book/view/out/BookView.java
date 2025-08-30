package ru.rerumu.lists.controller.book.view.out;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import ru.rerumu.lists.controller.DeepCopyable;
import ru.rerumu.lists.controller.author.out.AuthorView2;
import ru.rerumu.lists.controller.readingrecord.view.out.ReadingRecordView;
import ru.rerumu.lists.controller.tag.view.out.TagView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
@ToString
public class BookView implements DeepCopyable<BookView>{

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

    @Builder(toBuilder = true, access = AccessLevel.PRIVATE)
    @Getter
    public static class BookType implements DeepCopyable<BookType> {

        private final Integer typeId;
        private final String typeName;

        public BookType(@NonNull Integer typeId, @NonNull String typeName) {
            this.typeId = typeId;
            this.typeName = typeName;
        }

        @Override
        public BookType deepCopy() {
            return this.toBuilder().build();
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

    @Getter
    private final List<AuthorView2> textAuthors;

    @Setter
    @Getter
    private List<SeriesView> seriesList;

    @Builder(toBuilder = true, access = AccessLevel.PACKAGE)
    BookView(
            @NonNull Long bookId,
            Long readListId,
            @NonNull String title,
            BookStatusView bookStatus,
            @NonNull LocalDateTime insertDate,
            @NonNull LocalDateTime lastUpdateDate,
            Integer lastChapter,
            String note,
            BookType bookType,
            @NonNull String itemType,
            @NonNull List<BookView> chain,
            @NonNull List<ReadingRecordView> readingRecords,
            String URL,
            @NonNull List<TagView> tags,
            List<AuthorView2> textAuthors,
            List<SeriesView> seriesList
    ) {
        this.bookId = bookId;
        this.readListId = readListId;
        this.title = title;
        this.textAuthors = textAuthors;
        if (bookStatus != null) {
            this.bookStatus = bookStatus.deepCopy();
        } else {
            this.bookStatus = null;
        }
        this.insertDate = insertDate;
        this.lastUpdateDate = lastUpdateDate;
        this.lastChapter = lastChapter;
        this.note = note;

        if (bookType != null) {
            this.bookType = bookType.deepCopy();
        } else {
            this.bookType = null;
        }

        this.itemType = itemType;

        this.chain = chain.stream()
                .map(BookView::deepCopy)
                .collect(Collectors.toCollection(ArrayList::new));

        this.readingRecords = readingRecords.stream()
                .map(ReadingRecordView::deepCopy)
                .collect(Collectors.toCollection(ArrayList::new));

        this.URL = URL;

        this.tags = tags.stream()
                .map(TagView::deepCopy)
                .collect(Collectors.toCollection(ArrayList::new));

        this.seriesList = seriesList.stream()
                .map(SeriesView::deepCopy)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public BookView deepCopy() {
        BookView.BookViewBuilder builder = this.toBuilder()
                .bookStatus(bookStatus.deepCopy())
                .chain(chain.stream()
                        .map(BookView::deepCopy)
                        .collect(Collectors.toCollection(ArrayList::new))
                )
                .readingRecords(readingRecords.stream()
                        .map(ReadingRecordView::deepCopy)
                        .collect(Collectors.toCollection(ArrayList::new))
                )
                .tags(tags.stream()
                        .map(TagView::deepCopy)
                        .collect(Collectors.toCollection(ArrayList::new))
                )
                ;

        if (bookType != null) {
            builder.bookType(bookType.deepCopy());
        }

        return builder.build();
    }
}

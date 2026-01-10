package ru.rerumu.lists.controller.book.view.out;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.controller.author.views.out.AuthorView;
import ru.rerumu.lists.controller.tag.view.out.TagView;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.services.book.Search;
import ru.rerumu.lists.services.book.SearchOrder;
import ru.rerumu.lists.services.book.SortItem;
import ru.rerumu.lists.domain.readingrecord.ReadingRecord;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.seriesitem.SeriesItemType;
import ru.rerumu.lists.domain.tag.Tag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BookViewFactory {

    public BookView buildBookView(@NonNull Book book) {

        List<BookView> chain;
        if (book.getPreviousBooks() != null) {
            chain = book.getPreviousBooks().map().entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .map(entry -> buildBookView(entry.getKey()))
                    .collect(Collectors.toCollection(ArrayList::new));

        } else {
            chain = new ArrayList<>();
        }

        List<ReadingRecordView> readingRecordViews = book.getReadingRecords().stream()
                .sorted(Comparator.comparing(ReadingRecord::getStartDate))
                .map(readingRecord -> new ReadingRecordView(
                        readingRecord.getId(),
                        readingRecord.getBookId(),
                        new BookStatusView(
                                readingRecord.getBookStatus().getId().intValue(),
                                readingRecord.getBookStatus().getName()
                        ),
                        readingRecord.getStartDate(),
                        readingRecord.getEndDate(),
                        readingRecord.getIsMigrated(),
                        readingRecord.getLastChapter(),
                        readingRecord.getUpdateDate()
                ))
                .collect(Collectors.toCollection(ArrayList::new));

        ReadingRecordView maxRecord = readingRecordViews.stream()
                .max(Comparator.comparing(ReadingRecordView::getStartDate))
                .orElseThrow(() -> new ServerException("Error while processing records"));

        BookView.BookType bookType = null;
        if (book.getBookType() != null) {
            bookType = new BookView.BookType(
                    book.getBookType().getId(),
                    book.getBookType().getName()
            );
        }

        List<TagView> tagViews = book.getTags().stream()
                .sorted(Comparator.comparing(Tag::getName))
                .map(tag -> new TagView(
                        tag.getId(),
                        tag.getName()
                ))
                .collect(Collectors.toCollection(ArrayList::new));

        List<AuthorView> textAuthors = book.getTextAuthors().stream()
                .sorted(Comparator.comparing(Author::getName))
                .map(AuthorView::new)
                .collect(Collectors.toCollection(ArrayList::new));

        List<SeriesView> seriesViewList = book.getSeriesList().stream()
                .sorted(Comparator.comparing(Series::getTitle))
                .map(series -> new SeriesView(series.getId(), series.getTitle()))
                .collect(Collectors.toCollection(ArrayList::new));

        return new BookView(
                book.getId(),
                null,
                book.getTitle(),
                maxRecord.getBookStatus(),
                book.getInsertDate(),
                null,
                book.getNote(),
                bookType,
                SeriesItemType.BOOK.name(),
                chain,
                readingRecordViews,
                book.getURL(),
                tagViews,
                textAuthors,
                seriesViewList
        );
    }

    public BookListView buildBookListView(List<Book> books, Search search) {

        Comparator<Book> comparator = Comparator.comparing(book -> 0);

        for (SortItem sortItem : search.getSortItemList()) {
            if (sortItem.getSortField().equals("createDate")) {

                comparator = comparator.thenComparing(Book::getInsertDate);

                if (sortItem.getSearchOrder() == SearchOrder.DESC) {
                    comparator = comparator.reversed();
                }
            }

            if (sortItem.getSortField().equals("readingRecords.updateDate") ) {

                comparator = comparator.thenComparing( book -> {
                    ReadingRecord readingRecord = book.getReadingRecords().stream()
                            // Get most recent record
                            .max(Comparator.comparing(ReadingRecord::getStartDate))
                            .orElseThrow(() -> new ServerException("Error while processing records"));

                    // Compare update date
                    return readingRecord.getUpdateDate();
                });

                if (sortItem.getSearchOrder() == SearchOrder.DESC) {
                    comparator = comparator.reversed();
                }
            }
        }

        comparator = comparator.thenComparing(Book::getId);

        return new BookListView(
                books.stream()
                        .sorted(comparator)
                        .map(this::buildBookView)
                        .collect(Collectors.toCollection(ArrayList::new))
        );
    }

    public BookView buildBookView(@NonNull Book book, List<Series> seriesList) {
        BookView bookView = buildBookView(book);

        List<SeriesView> seriesViews = seriesList.stream()
                .map(series -> new SeriesView(series.getId(), series.getTitle()))
                .collect(Collectors.toCollection(ArrayList::new));

        bookView.setSeriesList(seriesViews);

        return bookView;
    }
}

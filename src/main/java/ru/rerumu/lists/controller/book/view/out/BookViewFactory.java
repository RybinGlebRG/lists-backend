package ru.rerumu.lists.controller.book.view.out;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.controller.author.views.out.AuthorView;
import ru.rerumu.lists.controller.tag.view.out.TagView;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.readingrecord.ReadingRecord;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.seriesitem.SeriesItemType;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.services.book.Search;
import ru.rerumu.lists.services.book.SearchOrder;
import ru.rerumu.lists.services.book.SortItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BookViewFactory {

    public BookView buildBookView(@NonNull Book book) {

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
                new ArrayList<>(),
                readingRecordViews,
                book.getURL(),
                tagViews,
                textAuthors,
                seriesViewList
        );
    }

    public BookView buildBookView(@NonNull Book book, @NonNull List<Book> previousBooks) {

        List<BookView> chain = previousBooks.stream()
                .map(this::buildBookView)
                .collect(Collectors.toCollection(ArrayList::new));

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

                    // Get most recent record
                    ReadingRecord readingRecord = book.getReadingRecords().stream()
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

        // Chain books by series
        if (search.isChainBySeries()) {
            Map<Book, List<Book>> booksChain = chainBooksBySeries(books);

            Comparator<Book> finalComparator = comparator;
            Comparator<Map.Entry<Book, List<Book>>> mapEntryComparator = (e1, e2) -> finalComparator.compare(e1.getKey(), e2.getKey());

            List<BookView> bookViewList = booksChain.entrySet().stream()
                    .sorted(mapEntryComparator)
                    .map(item -> buildBookView(item.getKey(), item.getValue()))
                    .collect(Collectors.toCollection(ArrayList::new));
            return new BookListView(bookViewList);
        } else {
            return new BookListView(
                    books.stream()
                            .sorted(comparator)
                            .map(this::buildBookView)
                            .collect(Collectors.toCollection(ArrayList::new))
            );
        }
    }

    public BookListView buildBookListView(Map<Book, List<Book>> booksBySeries, Search search) {

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

                    // Get most recent record
                    ReadingRecord readingRecord = book.getReadingRecords().stream()
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

        Comparator<Book> finalComparator = comparator;
        Comparator<Map.Entry<Book, List<Book>>> mapEntryComparator = (e1, e2) -> finalComparator.compare(e1.getKey(), e2.getKey());

        List<BookView> bookViewList = booksBySeries.entrySet().stream()
                .sorted(mapEntryComparator)
                .map(item -> buildBookView(item.getKey(), item.getValue()))
                .collect(Collectors.toCollection(ArrayList::new));
        return new BookListView(bookViewList);

    }

    @NonNull
    public Map<Book, List<Book>> chainBooksBySeries(@NonNull List<Book> books) {
        Map<Series, List<Book>> series2booksMap = new HashMap<>();

        for (Book book: books) {

            // If book is not in series
            if (book.getSeriesList().isEmpty()) {

                // Initialize list for null key
                if (!series2booksMap.containsKey(null)) {
                    series2booksMap.put(null, new ArrayList<>());
                }


                series2booksMap.get(null).add(book);
            } else {

                // For each series book is in
                for (Series series: book.getSeriesList()) {

                    // Initialize list for key
                    if (!series2booksMap.containsKey(series)) {
                        series2booksMap.put(series, new ArrayList<>());
                    }

                    series2booksMap.get(series).add(book);
                }
            }
        }

        // Find last book in series
        Comparator<Book> booksComparator = Comparator.comparing( (Book book) -> {
            // Get most recent record
            ReadingRecord readingRecord = book.getReadingRecords().stream()
                    .max(Comparator.comparing(ReadingRecord::getStartDate))
                    .orElseThrow(() -> new ServerException("Error while processing records"));

            // Compare update date
            return readingRecord.getUpdateDate();
        }).reversed();

        List<Book> booksWithoutSeries = new ArrayList<>();

        if (series2booksMap.get(null) != null) {
            booksWithoutSeries.addAll(series2booksMap.get(null));
            series2booksMap.remove(null);
        }

        series2booksMap.forEach((series, booksList) -> booksList.sort(booksComparator));

        Map<Book, List<Book>> bookChain = new HashMap<>();
        for (Map.Entry<Series, List<Book>> entry: series2booksMap.entrySet()) {
            Book lastBookInSeries = entry.getValue().get(0);

            List<Book> previousBooks = new ArrayList<>(entry.getValue());
            previousBooks.remove(lastBookInSeries);

            bookChain.put(lastBookInSeries, previousBooks);
        }

        for (Book book: booksWithoutSeries) {
            bookChain.put(book, new ArrayList<>());
        }

        return bookChain;
    }
}

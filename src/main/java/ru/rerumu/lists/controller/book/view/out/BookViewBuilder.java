package ru.rerumu.lists.controller.book.view.out;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.controller.readingrecord.view.out.ReadingRecordView;
import ru.rerumu.lists.controller.tag.view.out.TagView;
import ru.rerumu.lists.model.book.BookDTO;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.model.books.SearchOrder;
import ru.rerumu.lists.model.books.SortItem;
import ru.rerumu.lists.model.dto.BookOrderedDTO;
import ru.rerumu.lists.model.series.item.SeriesItemType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookViewBuilder {

    public BookView buildBookView(@NonNull BookDTO bookDTO, @NonNull Search search) {

        List<BookView> chain = new ArrayList<>();

        if (bookDTO.getPreviousBooks() != null) {
            chain = bookDTO.getPreviousBooks().stream()
                    .sorted(Comparator.comparing(BookOrderedDTO::getOrder))
                    .map(BookOrderedDTO::getBookDTO)
                    .map(item -> buildBookView(item, search))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        List<ReadingRecordView> readingRecordViews = new ArrayList<>();

        if (bookDTO.getReadingRecords() != null) {
            readingRecordViews =  bookDTO.getReadingRecords().stream()
                    .map(readingRecordDTO -> new ReadingRecordView(
                            readingRecordDTO.recordId(),
                            readingRecordDTO.bookId(),
                            new BookStatusView(
                                    readingRecordDTO.bookStatus().statusId(),
                                    readingRecordDTO.bookStatus().statusName()
                            ),
                            readingRecordDTO.startDate(),
                            readingRecordDTO.endDate(),
                            readingRecordDTO.isMigrated(),
                            readingRecordDTO.lastChapter()
                    ))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        BookView.BookType bookType = null;

        if (bookDTO.getBookTypeObj() != null) {
            bookType = new BookView.BookType(
                    bookDTO.getBookTypeObj().id,
                    bookDTO.getBookTypeObj().name
            );
        }


        BookView bookView = new BookView(
                bookDTO.getBookId(),
                bookDTO.getReadListId(),
                bookDTO.getTitle(),
                new BookStatusView(
                        bookDTO.getBookStatusObj().statusId(),
                        bookDTO.getBookStatusObj().statusName()
                ),
                bookDTO.getLastInsertLocalDate(),
                bookDTO.getLastUpdateDate_V2(),
                bookDTO.lastChapter,
                bookDTO.getNote(),
                bookType,
                SeriesItemType.BOOK.name(),
                chain,
                readingRecordViews,
                bookDTO.getURL(),
                bookDTO.getTags().stream()
                        .map(tagDTO -> new TagView(
                                tagDTO.getTagId(),
                                tagDTO.getName()
                        ))
                        .collect(Collectors.toCollection(ArrayList::new))
        );

        return bookView;
    }

    public BookListView buildBookListView(List<BookDTO> bookDTOList, Search search) {


        Comparator<BookDTO> comparator = Comparator.comparing(book -> 0);

        for (SortItem sortItem : search.getSortItemList()) {
            if (sortItem.getSortField().equals("createDate")) {

                comparator = comparator.thenComparing(BookDTO::getInsertDate);

                if (sortItem.getSearchOrder() == SearchOrder.DESC) {
                    comparator = comparator.reversed();
                }
            }
        }

        comparator = comparator.thenComparing(BookDTO::getBookId);

         return new BookListView(
                 bookDTOList.stream()
                    .sorted(comparator)
                    .map(bookDTO -> buildBookView(bookDTO, search))
                    .collect(Collectors.toCollection(ArrayList::new))
         );
    }
}

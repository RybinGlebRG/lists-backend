package ru.rerumu.lists.controller.book.view.out;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.controller.readingrecord.view.out.ReadingRecordView;
import ru.rerumu.lists.controller.tag.view.out.TagView;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.model.book.BookDTO;
import ru.rerumu.lists.model.book.readingrecords.ReadingRecordDTO;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.model.books.SearchOrder;
import ru.rerumu.lists.model.books.SortItem;
import ru.rerumu.lists.model.dto.BookOrderedDTO;
import ru.rerumu.lists.model.series.item.SeriesItemType;
import ru.rerumu.lists.model.tag.TagDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BookViewFactory {

    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    public BookView buildBookView(@NonNull BookDTO bookDTO, @NonNull Search search) {

        List<BookView> chain = bookDTO.getPreviousBooks().stream()
                .sorted(Comparator.comparing(BookOrderedDTO::getOrder))
                .map(BookOrderedDTO::getBookDTO)
                .map(item -> buildBookView(item, search))
                .collect(Collectors.toCollection(ArrayList::new));

        List<ReadingRecordView> readingRecordViews = bookDTO.getReadingRecords().stream()
                .sorted(Comparator.comparing(ReadingRecordDTO::startDate))
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

        ReadingRecordView maxRecord = readingRecordViews.stream()
                .max(Comparator.comparing(ReadingRecordView::getStartDate))
                .orElseThrow(() -> new ServerException("Error while processing records"));

        BookView.BookType bookType = null;
        if (bookDTO.getBookTypeObj() != null) {
            bookType = new BookView.BookType(
                    bookDTO.getBookTypeObj().id,
                    bookDTO.getBookTypeObj().name
            );
        }

        List<TagView> tagViews = bookDTO.getTags().stream()
                .sorted(Comparator.comparing(TagDTO::getName))
                .map(tagDTO -> new TagView(
                        tagDTO.getTagId(),
                        tagDTO.getName()
                ))
                .collect(Collectors.toCollection(ArrayList::new));

        BookView bookView = new BookView(
                bookDTO.getBookId(),
                bookDTO.getReadListId(),
                bookDTO.getTitle(),
                maxRecord.getBookStatus(),
                bookDTO.getLastInsertLocalDate(),
                bookDTO.getLastUpdateDate_V2(),
                bookDTO.lastChapter,
                bookDTO.getNote(),
                bookType,
                SeriesItemType.BOOK.name(),
                chain,
                readingRecordViews,
                bookDTO.getURL(),
                tagViews
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

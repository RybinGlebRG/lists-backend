package ru.rerumu.lists.views.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import ru.rerumu.lists.model.book.Book;
import ru.rerumu.lists.model.book.BookDTO;
import ru.rerumu.lists.model.book.impl.BookImpl;
import ru.rerumu.lists.model.books.Filter;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.model.books.SearchOrder;
import ru.rerumu.lists.model.books.SortItem;

import java.util.Comparator;
import java.util.List;

public class BookListView {

    private final List<BookDTO> bookList;
    private final List<SortItem> sortItemList;
    private final Search search;
    private final ObjectMapper objectMapper;

    public BookListView(
            @NonNull List<BookDTO> bookList,
            List<SortItem> sortItemList,
            Search search, ObjectMapper objectMapper
    ) {
        this.bookList = bookList;
        this.sortItemList = sortItemList;
        this.search = search;
        this.objectMapper = objectMapper;
    }

    public void sort() {
        boolean isSearch = false;
        if (search != null && search.filters() != null) {
            for (Filter filter : search.filters()) {
                if (filter.field().equals("titles")) {
                    isSearch = true;
                }
            }
        }
        if (isSearch){
            return;
        }
        Comparator<BookDTO> comparator = Comparator.comparing(bookDTO -> 0);

        for (SortItem sortItem : sortItemList) {
            if (sortItem.getSortField().equals("createDate")) {

                comparator = comparator.thenComparing(BookDTO::getInsertDate);

                if (sortItem.getSearchOrder() == SearchOrder.DESC) {
                    comparator = comparator.reversed();
                }
            }
        }

        comparator = comparator.thenComparing(BookDTO::getBookId);

        this.bookList.sort(comparator);
    }

    public String toJsonString() {

    }
}

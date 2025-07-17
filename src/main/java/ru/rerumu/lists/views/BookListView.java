package ru.rerumu.lists.views;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.book.impl.BookImpl;
import ru.rerumu.lists.domain.books.Filter;
import ru.rerumu.lists.domain.books.Search;
import ru.rerumu.lists.domain.books.SearchOrder;
import ru.rerumu.lists.domain.books.SortItem;
import ru.rerumu.lists.domain.series.impl.SeriesImpl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class BookListView {

    private final List<Book> bookList;
    private final Map<BookImpl, List<SeriesImpl>> bookSeriesMap;

    private final Boolean isChainBySeries;

    private final List<SortItem> sortItemList;

    private final Search search;

    public BookListView(List<Book> bookList, Map<BookImpl, List<SeriesImpl>> bookSeriesMap, Boolean isChainBySeries, List<SortItem> sortItemList, Search search) {
        this.bookList = bookList;
        this.bookSeriesMap = bookSeriesMap;
        this.isChainBySeries = isChainBySeries;
        this.sortItemList = sortItemList;
        this.search = search;
    }

//    public void sort() {
//        Comparator<Object> comparator = Comparator
//                .comparing(book -> ((BookImpl)book).getInsertDate())
//                .thenComparing(book -> ((BookImpl)book).getTitle())
//                .thenComparing(book -> ((BookImpl)book).getId());
//
//        this.bookList.sort(comparator);
//    }

    public void sort(List<SortItem> sortItemList) {

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
        Comparator<Book> comparator = Comparator.comparing(book -> 0);

        for (SortItem sortItem : sortItemList) {
            if (sortItem.getSortField().equals("createDate")) {

                comparator = comparator.thenComparing(book -> ((BookImpl)book).getInsertDate());

                if (sortItem.getSearchOrder() == SearchOrder.DESC) {
                    comparator = comparator.reversed();
                }
            }
        }

        comparator = comparator.thenComparing(Book::getId);

        this.bookList.sort(comparator);
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        JSONArray bookArray = bookList.stream()
                .map(Book::toJSONObject)
                .collect(JSONArray::new, JSONArray::put, JSONArray::putAll);

        obj.put("items", bookArray);
        return obj;
    }

    @Override
    public String toString() {

        return this.toJSONObject().toString();
    }

    public static class Builder {
        private Map<BookImpl, List<SeriesImpl>> bookSeriesMap;
        private List<Book> bookList;

        private Boolean isChainBySeries;

        private List<SortItem> sortItemList;

        private Search search;

        public Builder bookSeriesMap(Map<BookImpl, List<SeriesImpl>> bookSeriesMap) {
            this.bookSeriesMap = bookSeriesMap;
            return this;
        }

        public Builder bookList(List<Book> bookList) {
            this.bookList = bookList;
            return this;
        }

        public Builder isChainBySeries(Boolean isChainBySeries) {
            this.isChainBySeries = isChainBySeries;
            return this;
        }

        public Builder sort(List<SortItem> sortItemList) {
            this.sortItemList = sortItemList;
            return this;
        }
        public Builder search(Search search) {
            this.search = search;
            return this;
        }

        public BookListView build() {
            if (isChainBySeries == null) {
                isChainBySeries = false;
            }
            return new BookListView(bookList, bookSeriesMap, isChainBySeries, sortItemList, search);
        }
    }

}

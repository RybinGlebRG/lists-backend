package ru.rerumu.lists.views;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.books.Filter;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.model.books.SearchOrder;
import ru.rerumu.lists.model.books.SortItem;

import java.util.*;
import java.util.stream.Collectors;

public class BookListView {

    private final List<Book> bookList;
    private final Map<Book, List<Series>> bookSeriesMap;

    private final Boolean isChainBySeries;

    private final List<SortItem> sortItemList;

    private final Search search;

    public BookListView(List<Book> bookList, Map<Book, List<Series>> bookSeriesMap, Boolean isChainBySeries, List<SortItem> sortItemList, Search search) {
        this.bookList = bookList;
        this.bookSeriesMap = bookSeriesMap;
        this.isChainBySeries = isChainBySeries;
        this.sortItemList = sortItemList;
        this.search = search;
    }

    public void sort() {
        Comparator<Book> comparator = Comparator
                .comparing(Book::getInsertDate)
                .thenComparing(Book::getTitle)
                .thenComparing(Book::getBookId);

        this.bookList.sort(comparator);
    }

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

                comparator = comparator.thenComparing(Book::getInsertDate);

                if (sortItem.getSearchOrder() == SearchOrder.DESC) {
                    comparator = comparator.reversed();
                }
            }
        }

        comparator = comparator.thenComparing(Book::getBookId);

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
        private Map<Book, List<Series>> bookSeriesMap;
        private List<Book> bookList;

        private Boolean isChainBySeries;

        private List<SortItem> sortItemList;

        private Search search;

        public Builder bookSeriesMap(Map<Book, List<Series>> bookSeriesMap) {
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

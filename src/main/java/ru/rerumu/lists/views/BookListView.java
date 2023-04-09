package ru.rerumu.lists.views;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.books.SearchOrder;
import ru.rerumu.lists.model.books.SortItem;

import java.util.*;
import java.util.stream.Collectors;

public class BookListView {

    private final List<Book> bookList;
    private final Map<Book,List<Series>> bookSeriesMap;

    private final Boolean isChainBySeries;

    private final List<SortItem> sortItemList;

    public BookListView(List<Book> bookList, Map<Book, List<Series>> bookSeriesMap, Boolean isChainBySeries, List<SortItem> sortItemList) {
        this.bookList = bookList;
        this.bookSeriesMap = bookSeriesMap;
        this.isChainBySeries = isChainBySeries;
        this.sortItemList = sortItemList;
    }

    public void sort() {
        Comparator<Book> comparator = Comparator
                .comparing(Book::getInsertDate)
                .thenComparing(Book::getTitle)
                .thenComparing(Book::getBookId);

        this.bookList.sort(comparator);
    }

    public void sort(List<SortItem> sortItemList) {
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

//    private JSONArray formatSeriesList(Book book){
//        JSONArray arr = new JSONArray();
//        for(Series series: bookSeriesMap.get(book)){
//            arr.put(series.toJSONObject("seriesId","title"));
//        }
//        return arr;
//    }

    private JSONArray toChainBySeries(List<Book> bookList){
        Set<List<Series>> processedSeries = new HashSet<>();

        JSONArray res = new JSONArray();

        for(Book book: bookList){
            List<Series> bookSeries = bookSeriesMap.get(book);

            if (!processedSeries.contains(bookSeries)) {
                JSONArray booksChain = bookList.stream()
                        .filter(item -> !item.equals(book))
                        .filter(item -> bookSeriesMap.get(item).stream().anyMatch(bookSeries::contains) || bookSeriesMap.get(item).size() == 0)
                        .map(Book::toJSONObject)
                        .collect(JSONArray::new, JSONArray::put, JSONArray::putAll);
                JSONObject bookObj = book.toJSONObject()
                        .put("chain", booksChain);
                res.put(bookObj);
                processedSeries.add(bookSeries);
            }
        }
        return res;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        JSONArray bookArray;
        if (isChainBySeries){
            bookArray = toChainBySeries(bookList);
        } else {
            bookArray = bookList.stream()
                    .map(Book::toJSONObject)
                    .collect(JSONArray::new,JSONArray::put,JSONArray::putAll);
        }

        obj.put("items", bookArray);
        return obj;
    }

    @Override
    public String toString() {

        return this.toJSONObject().toString();
    }

    public static class Builder{
        private Map<Book,List<Series>> bookSeriesMap;
        private List<Book> bookList;

        private Boolean isChainBySeries;

        private List<SortItem> sortItemList;

        public Builder bookSeriesMap(Map<Book,List<Series>> bookSeriesMap){
            this.bookSeriesMap = bookSeriesMap;
            return this;
        }

        public Builder bookList(List<Book> bookList){
            this.bookList = bookList;
            return this;
        }

        public Builder isChainBySeries(Boolean isChainBySeries){
            this.isChainBySeries = isChainBySeries;
            return this;
        }

        public Builder sort(List<SortItem> sortItemList)
        {
            this.sortItemList = sortItemList;
            return this;
        }

        public BookListView build(){
            if (isChainBySeries == null){
                isChainBySeries=false;
            }
            return new BookListView(bookList, bookSeriesMap, isChainBySeries, sortItemList);
        }
    }

}

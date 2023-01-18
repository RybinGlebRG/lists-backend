package ru.rerumu.lists.views;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.books.SearchOrder;
import ru.rerumu.lists.model.books.SortItem;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class BookListView {

    private final List<Book> bookList;
    private final Map<Book,List<Series>> bookSeriesMap;

    public BookListView(
            List<Book> bookList,
            Map<Book,List<Series>> bookSeriesMap
    ) {
        this.bookList = bookList;
        this.bookSeriesMap = bookSeriesMap;
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

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        JSONArray bookArray = new JSONArray();
        for (Book item : this.bookList) {
            JSONObject bookObj = item.toJSONObject()
//                    .put("seriesList",formatSeriesList(item))
                    ;

            bookArray.put(bookObj);
        }
        obj.put("items", bookArray);
        return obj;
    }

    @Override
    public String toString() {

        return this.toJSONObject().toString();
    }

    public static class Builder{
//        private Map<Book,List<Series>> bookSeriesMap;
        private List<Book> bookList;

//        public Builder bookSeriesMap(Map<Book,List<Series>> bookSeriesMap){
//            this.bookSeriesMap = bookSeriesMap;
//            return this;
//        }

        public Builder bookList(List<Book> bookList){
            this.bookList = bookList;
            return this;
        }

        public BookListView build(){
            return new BookListView(bookList, null);
        }
    }

}

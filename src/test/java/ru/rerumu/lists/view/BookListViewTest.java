package ru.rerumu.lists.view;


import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.BookStatus;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.model.books.SearchOrder;
import ru.rerumu.lists.model.books.SortItem;
import ru.rerumu.lists.views.BookListView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class BookListViewTest {

    @Test
    void shouldChain() throws Exception {
        List<SortItem> sortItemList = new ArrayList<>();
        sortItemList.add(new SortItem("createDate", SearchOrder.DESC));
        Map<Book, List<Series>> bookSeriesMap = new HashMap<>();

        List<Book> bookList = new ArrayList<>();

        Book book1 = new Book.Builder()
                .bookId(1L)
                .title("Test1")
                .lastUpdateDate(LocalDateTime.of(2023, 4, 8, 22, 35))
                .insertDate(LocalDateTime.of(2023, 4, 8, 22, 35))
                .bookStatus(new BookStatusRecord(2,"Completed"))
                .build();
        Book book2 = new Book.Builder()
                .bookId(2L)
                .title("Test2")
                .lastUpdateDate(LocalDateTime.of(2023, 4, 9, 22, 35))
                .insertDate(LocalDateTime.of(2023, 4, 9, 22, 35))
                .bookStatus(new BookStatusRecord(2,"Completed"))
                .build();

        bookList.add(book1);
        bookList.add(book2);

        List<Series> seriesList = new ArrayList<>();
        Series series = new Series.Builder()
                .seriesId(5L)
                .title("Series1")
                .readListId(2L)
                .itemList(List.of(book1,book2))
                .build();

        seriesList.add(series);

        bookSeriesMap.put(book1, seriesList);
        bookSeriesMap.put(book2, seriesList);

        BookListView bookListView = new BookListView.Builder()
                .bookList(bookList)
                .bookSeriesMap(bookSeriesMap)
                .isChainBySeries(true)
                .sort(sortItemList)
                .build();
        bookListView.sort(sortItemList);
        JSONObject res = bookListView.toJSONObject();

        JSONArray array = new JSONArray();


        Assertions.assertEquals(1,((JSONArray)res.get("items")).length());
    }

    @Test
    void shouldNotChainDifferentSeries() throws Exception {
        List<SortItem> sortItemList = new ArrayList<>();
        sortItemList.add(new SortItem("createDate", SearchOrder.DESC));
        Map<Book, List<Series>> bookSeriesMap = new HashMap<>();

        List<Book> bookList = new ArrayList<>();

        Book book1 = new Book.Builder()
                .bookId(1L)
                .title("Test1")
                .lastUpdateDate(LocalDateTime.of(2023, 4, 8, 22, 35))
                .insertDate(LocalDateTime.of(2023, 4, 8, 22, 35))
                .bookStatus(new BookStatusRecord(2,"Completed"))
                .build();
        Book book2 = new Book.Builder()
                .bookId(2L)
                .title("Test2")
                .lastUpdateDate(LocalDateTime.of(2023, 4, 9, 22, 35))
                .insertDate(LocalDateTime.of(2023, 4, 9, 22, 35))
                .bookStatus(new BookStatusRecord(2,"Completed"))
                .build();

        bookList.add(book1);
        bookList.add(book2);

        Series series1 = new Series.Builder()
                .seriesId(5L)
                .title("Series1")
                .readListId(2L)
                .itemList(List.of(book1,book2))
                .build();

        Series series2 = new Series.Builder()
                .seriesId(6L)
                .title("Series2")
                .readListId(2L)
                .itemList(List.of(book1,book2))
                .build();

        bookSeriesMap.put(book1, List.of(series1));
        bookSeriesMap.put(book2, List.of(series2));

        BookListView bookListView = new BookListView.Builder()
                .bookList(bookList)
                .bookSeriesMap(bookSeriesMap)
                .isChainBySeries(true)
                .sort(sortItemList)
                .build();
        bookListView.sort(sortItemList);
        JSONObject res = bookListView.toJSONObject();

        Assertions.assertEquals(2,((JSONArray)res.get("items")).length());
    }

    @Test
    void shouldNotChainNoSeriesOne() throws Exception {
        List<SortItem> sortItemList = new ArrayList<>();
        sortItemList.add(new SortItem("createDate", SearchOrder.DESC));
        Map<Book, List<Series>> bookSeriesMap = new HashMap<>();

        List<Book> bookList = new ArrayList<>();

        Book book1 = new Book.Builder()
                .bookId(1L)
                .title("Test1")
                .lastUpdateDate(LocalDateTime.of(2023, 4, 8, 22, 35))
                .insertDate(LocalDateTime.of(2023, 4, 8, 22, 35))
                .bookStatus(new BookStatusRecord(2,"Completed"))
                .build();
        Book book2 = new Book.Builder()
                .bookId(2L)
                .title("Test2")
                .lastUpdateDate(LocalDateTime.of(2023, 4, 9, 22, 35))
                .insertDate(LocalDateTime.of(2023, 4, 9, 22, 35))
                .bookStatus(new BookStatusRecord(2,"Completed"))
                .build();

        bookList.add(book1);
        bookList.add(book2);

        Series series1 = new Series.Builder()
                .seriesId(5L)
                .title("Series1")
                .readListId(2L)
                .itemList(List.of(book1,book2))
                .build();

        bookSeriesMap.put(book1, List.of(series1));

        BookListView bookListView = new BookListView.Builder()
                .bookList(bookList)
                .bookSeriesMap(bookSeriesMap)
                .isChainBySeries(true)
                .sort(sortItemList)
                .build();
        bookListView.sort(sortItemList);
        JSONObject res = bookListView.toJSONObject();

        Assertions.assertEquals(2,((JSONArray)res.get("items")).length());
    }

    @Test
    void shouldNotChainNoSeriesOne2() throws Exception {
        List<SortItem> sortItemList = new ArrayList<>();
        sortItemList.add(new SortItem("createDate", SearchOrder.DESC));
        Map<Book, List<Series>> bookSeriesMap = new HashMap<>();

        List<Book> bookList = new ArrayList<>();

        Book book1 = new Book.Builder()
                .bookId(1L)
                .title("Test1")
                .lastUpdateDate(LocalDateTime.of(2023, 4, 8, 22, 35))
                .insertDate(LocalDateTime.of(2023, 4, 8, 22, 35))
                .bookStatus(new BookStatusRecord(2,"Completed"))
                .build();
        Book book2 = new Book.Builder()
                .bookId(2L)
                .title("Test2")
                .lastUpdateDate(LocalDateTime.of(2023, 4, 9, 22, 35))
                .insertDate(LocalDateTime.of(2023, 4, 9, 22, 35))
                .bookStatus(new BookStatusRecord(2,"Completed"))
                .build();

        bookList.add(book1);
        bookList.add(book2);

        Series series1 = new Series.Builder()
                .seriesId(5L)
                .title("Series1")
                .readListId(2L)
                .itemList(List.of(book1,book2))
                .build();

        bookSeriesMap.put(book2, List.of(series1));

        BookListView bookListView = new BookListView.Builder()
                .bookList(bookList)
                .bookSeriesMap(bookSeriesMap)
                .isChainBySeries(true)
                .sort(sortItemList)
                .build();
        bookListView.sort(sortItemList);
        JSONObject res = bookListView.toJSONObject();

        Assertions.assertEquals(2,((JSONArray)res.get("items")).length());
    }

    @Test
    void shouldNotChainNoSeriesBoth() throws Exception {
        List<SortItem> sortItemList = new ArrayList<>();
        sortItemList.add(new SortItem("createDate", SearchOrder.DESC));
        Map<Book, List<Series>> bookSeriesMap = new HashMap<>();

        List<Book> bookList = new ArrayList<>();

        Book book1 = new Book.Builder()
                .bookId(1L)
                .title("Test1")
                .lastUpdateDate(LocalDateTime.of(2023, 4, 8, 22, 35))
                .insertDate(LocalDateTime.of(2023, 4, 8, 22, 35))
                .bookStatus(new BookStatusRecord(2,"Completed"))
                .build();
        Book book2 = new Book.Builder()
                .bookId(2L)
                .title("Test2")
                .lastUpdateDate(LocalDateTime.of(2023, 4, 9, 22, 35))
                .insertDate(LocalDateTime.of(2023, 4, 9, 22, 35))
                .bookStatus(new BookStatusRecord(2,"Completed"))
                .build();

        bookList.add(book1);
        bookList.add(book2);

        BookListView bookListView = new BookListView.Builder()
                .bookList(bookList)
                .bookSeriesMap(bookSeriesMap)
                .isChainBySeries(true)
                .sort(sortItemList)
                .build();
        bookListView.sort(sortItemList);
        JSONObject res = bookListView.toJSONObject();

        Assertions.assertEquals(2,((JSONArray)res.get("items")).length());
    }
}

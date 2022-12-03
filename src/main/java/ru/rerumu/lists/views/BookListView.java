package ru.rerumu.lists.views;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.books.SearchOrder;
import ru.rerumu.lists.model.books.SortItem;

import java.util.Comparator;
import java.util.List;

public class BookListView {

    private final List<Book> bookList;

    public BookListView(List<Book> bookList) {
        this.bookList = bookList;
    }

    public void sort() {
        Comparator<Book> comparator = Comparator
                .comparing(Book::getInsertDate)
                .thenComparing(Book::getTitle)
                .thenComparing(Book::getBookId);

        this.bookList.sort(comparator);
    }

    public void sort(List<SortItem> sortItemList) {
        Comparator<Book> comparator=Comparator.comparing(book -> 0);

        for (SortItem sortItem: sortItemList){
            if (sortItem.getSortField().equals("createDate")){

                comparator.thenComparing(Book::getInsertDate);

                if (sortItem.getSearchOrder()== SearchOrder.DESC){
                    comparator.reversed();
                }
            }
        }

        comparator.thenComparing(Book::getBookId);

        this.bookList.sort(comparator);
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        JSONArray bookArray = new JSONArray();
        for (Book item : this.bookList) {
            bookArray.put(item.toJSONObject());
        }
        obj.put("items", bookArray);
        return obj;
    }

    @Override
    public String toString() {

        return this.toJSONObject().toString();
    }
}
